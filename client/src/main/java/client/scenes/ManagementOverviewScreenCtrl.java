package client.scenes;

import client.utils.ManagementOverviewUtils;
import client.utils.ServerUtils;
import client.utils.Translation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagementOverviewScreenCtrl implements Initializable {
    @FXML
    public TextField backupEventIDTextField;
    @FXML
    public Button importButton;
    @FXML
    public Label backupLabel;
    @FXML
    public Label backupEventFeedbackLabel;
    @FXML
    public Button exportButton;

    @FXML
    private Button homeScreenButton;
    @FXML
    private Label MOTitle;
    @FXML
    private Label eventsLabel;
    @FXML
    private Label participantsLabel;
    @FXML
    private Label expensesLabel;
    @FXML
    private ListView<Event> eventsListView;
    @FXML
    private ListView<Participant> participantsListView;
    @FXML
    private ListView<Expense> expensesListView;
    @FXML
    private Button sortButton;
    @FXML
    private ComboBox<StringProperty> orderTypeComboBox;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    private final ManagementOverviewUtils utils;

    /**
     * Constructor
     * @param server the ServerUtils instance
     * @param mainCtrl the MainCtrl instance
     * @param translation the Translation to use
     */
    @Inject
    public ManagementOverviewScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation,
                                        ManagementOverviewUtils utils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        this.utils = utils;
    }
    /**
     * Initialize basic features for the Management Overview Screen
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MOTitle.textProperty().bind(translation.getStringBinding("MOSCtrl.Title"));
        eventsLabel.textProperty().bind(translation.getStringBinding("MOSCtrl.Events.Label"));
        participantsLabel.textProperty().bind(translation.getStringBinding("MOSCtrl.Participants.Label"));
        expensesLabel.textProperty().bind(translation.getStringBinding("MOSCtrl.Expenses.Label"));
        importButton.textProperty().bind(translation.getStringBinding("MOSCtrl.ImportButton"));
        exportButton.textProperty().bind(translation.getStringBinding("MOSCtrl.ExportButton"));
        backupLabel.textProperty().bind(translation.getStringBinding("MOSCtrl.BackupLabel"));
        backupEventIDTextField.promptTextProperty().bind(translation.getStringBinding("MOSCtrl.BackupEventIDTextField"));
        backupEventFeedbackLabel.textProperty().bind(translation.getStringBinding("empty"));
        initializeSortButton();
        initializeOrderTypes();
        try{
            Image image = new Image(new FileInputStream("client/src/main/resources/images/home-page.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setPreserveRatio(true);
            homeScreenButton.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            System.out.println("didn't work");
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize a ListView with all events.
     */
    public void initializeAllEvents() {
        eventsListView.setItems(utils.retrieveEvents());
        eventsListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                if(empty || event == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText("Title: " + event.getTitle() + ", ID: " + event.getId());
                }
            }
        });
        eventsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldEvent, newEvent) -> {
            participantsListView.setItems(utils.initializeParticipantsList(newEvent));
            expensesListView.setItems(utils.initializeExpenseList(newEvent));
        });
    }

    /**
     * Initializes the sort button.
     */
    public void initializeSortButton() {
        sortButton.textProperty().bind(utils.bindSortButton());
    }

    /**
     * Initialize the ComboBox which contains all the ways that events can be ordered by.
     */
    public void initializeOrderTypes() {
        orderTypeComboBox.setItems(utils.setOrderTypes());
        Callback<ListView<StringProperty>, ListCell<StringProperty>> cellFactory = l -> new ListCell<>() {
            public void updateItem(StringProperty string, boolean empty) {
                super.updateItem(string, empty);
                if (empty || string == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(string.getValue());
                }
            }
        };
        orderTypeComboBox.setButtonCell(cellFactory.call(null));
        orderTypeComboBox.setCellFactory(cellFactory);
        orderTypeComboBox.getSelectionModel().select(0);
        orderTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldString, newString) ->
                utils.sortEventsSameOrder(newString));
    }

    /**
     * Sort button handler.
     */
    public void orderEvents() {
        utils.sortEventsOtherOrder(orderTypeComboBox.getValue());
    }

    /**
     * Gets the text from a given textfield
     * @param textBox the textfield to get the text from
     * @return String the text from the textfield
     */
    public String getTextBoxText(TextField textBox){
        return textBox.getText();
    }

    /**
     * Export the event to a backup file
     */
    @FXML
    private void exportButtonClicked() {
        backupEventFeedbackLabel.textProperty().bind(translation.getStringBinding("empty"));
        String eventId = getTextBoxText(backupEventIDTextField);
        Event event;
        try{
            event = server.getEvent(eventId);
        }catch (Exception e){
            backupEventFeedbackLabel.textProperty().bind(translation.getStringBinding("MOSCtrl.EventNotFound"));
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            // Write object to JSON file
            File backupFile = new File(String.format("./backups/%s.json", eventId));
            objectMapper.writeValue(backupFile, event);
            System.out.printf("Event %s has been exported to %s.json%n", eventId, eventId);
            backupEventFeedbackLabel.textProperty().bind(translation.getStringBinding("MOSCtrl.SuccessExport"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Import the event from a backup file
     */
    @FXML
    private void importButtonClicked() {
        backupEventFeedbackLabel.textProperty().bind(translation.getStringBinding("empty"));
        String eventId = getTextBoxText(backupEventIDTextField);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            // Read JSON data from file and deserialize it into object
            File backupFile = new File(String.format("./backups/%s.json", eventId));
            Event event = objectMapper.readValue(backupFile, Event.class);
            System.out.println("Read from file: " + event);
            backupEventFeedbackLabel.textProperty().bind(translation.getStringBinding("MOSCtrl.SuccessImport"));
            Event responseEvent = server.addEvent(event);
            initializeAllEvents();

        } catch (IOException e) {
            e.printStackTrace();
            backupEventFeedbackLabel.textProperty().bind(translation.getStringBinding("MOSCtrl.ErrorImportingEvent"));
        }
    }

    /**
     * switch back to main Screen
     * @param actionEvent when button is pressed, go back to the main screen
     */
    public void goBackToHomeScreen(ActionEvent actionEvent) {
        mainCtrl.switchBackToMainScreen();
    }
}
