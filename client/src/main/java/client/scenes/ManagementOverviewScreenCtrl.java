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
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagementOverviewScreenCtrl implements Initializable {
    protected ObjectMapper objectMapper;
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
    @FXML
    private Button deleteEventsButton;
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
        objectMapper = new ObjectMapper();
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
        bindButton(exportButton, "MOSCtrl.ExportButton");
        bindButton(importButton, "MOSCtrl.ImportButton");
        bindLabel(backupLabel, "MOSCtrl.BackupLabel");
        bindTextField(backupEventIDTextField, "MOSCtrl.BackupEventIDTextField");
        bindLabel(backupEventFeedbackLabel, "empty");
        deleteEventsButton.textProperty().bind(translation.getStringBinding("MOSCtrl.Delete.Events.Button"));
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
        participantsListView.setCellFactory(listView -> new ListCell<>(){
            @Override
            protected void updateItem(Participant participant, boolean empty){
                super.updateItem(participant, empty);
                if(empty || participant == null){
                    setText(null);
                    setGraphic(null);
                }
                else{
                    setText("Participant: " + participant.getName());
                }
            }
        });
        expensesListView.setCellFactory(listView -> new ListCell<>(){
            @Override
            protected void updateItem(Expense expense, boolean empty){
                super.updateItem(expense, empty);
                if(empty || expense == null){
                    setText(null);
                    setGraphic(null);
                }
                else{
                    String expenseString = expense.getOwedTo().getName() + " paid ";
                    expenseString+=(float)(expense.getPriceInCents()/100) + " euro for ";
                    expenseString+=expense.getName() + " on ";
                    expenseString+=expense.getDate().toString();
                    setText(expenseString);
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
     * Binds a string to a label
     * @param label the label
     * @param s the string
     */
    public void bindLabel(Label label, String s){
        label.textProperty().bind(translation.getStringBinding(s));
    }

    /**
     * Binds a string to a textfield
     * @param textField the textfield
     * @param s the string
     */
    public void bindTextField(TextField textField, String s) {
        textField.promptTextProperty().bind(translation.getStringBinding(s));
    }

    /**
     * Binds a string to a button
     * @param button the button
     * @param s the string
     */
    public void bindButton(Button button, String s) {
        button.textProperty().bind(translation.getStringBinding(s));
    }

    /**
     * Export the event to a backup file
     */
    @FXML
    public void exportButtonClicked() {
        bindLabel(backupEventFeedbackLabel, "empty");
        backupEventFeedbackLabel.getStyleClass().remove("successText");
        if (!backupEventFeedbackLabel.getStyleClass().contains("errorText")){
            backupEventFeedbackLabel.getStyleClass().add("errorText");
        }
        String eventId = getTextBoxText(backupEventIDTextField);
        Event event;
        try{
            event = server.getEvent(eventId);
        }catch (Exception e){
            bindLabel(backupEventFeedbackLabel, "MOSCtrl.EventNotFound");
            return;
        }
        objectMapper.registerModule(new JavaTimeModule());
        try {
            // Write object to JSON file
            File backupFile = new File(String.format("./backups/%s.json", eventId));
            objectMapper.writeValue(backupFile, event);
            System.out.printf("Event %s has been exported to %s.json%n", eventId, eventId);
            bindLabel(backupEventFeedbackLabel, "MOSCtrl.SuccessExport");
            backupEventFeedbackLabel.getStyleClass().remove("errorText");
            if (!backupEventFeedbackLabel.getStyleClass().contains("successText")){
                backupEventFeedbackLabel.getStyleClass().add("successText");
            }
        } catch (IOException e) {
            bindLabel(backupEventFeedbackLabel, "MOSCtrl.ErrorExportingEvent");
            System.out.printf("Error exporting event %s%n", eventId);
        }
    }

    /**
     * Import the event from a backup file
     */
    @FXML
    public void importButtonClicked() {
        bindLabel(backupEventFeedbackLabel, "empty");
        backupEventFeedbackLabel.getStyleClass().remove("successText");
        if (!backupEventFeedbackLabel.getStyleClass().contains("errorText")){
            backupEventFeedbackLabel.getStyleClass().add("errorText");
        }
        String eventId = getTextBoxText(backupEventIDTextField);
        objectMapper.registerModule(new JavaTimeModule());
        try {
            // Read JSON data from file and deserialize it into object
            File backupFile = readFile(eventId);
            Event event = objectMapper.readValue(backupFile, Event.class);
            System.out.println("Read from file: " + event);
            bindLabel(backupEventFeedbackLabel, "MOSCtrl.SuccessImport");
            server.addEvent(event);
            initializeAllEvents();
            backupEventFeedbackLabel.getStyleClass().remove("errorText");
            if (!backupEventFeedbackLabel.getStyleClass().contains("successText")){
                backupEventFeedbackLabel.getStyleClass().add("successText");
            }

        } catch (IOException e) {
            bindLabel(backupEventFeedbackLabel, "MOSCtrl.ErrorImportingEvent");
            System.out.printf("Error importing event %s%n", eventId);
        }
    }

    /**
     * Reads a backup file given an event id, if it exists
     * @param eventId the event id
     * @return File the backup file
     */
    public File readFile(String eventId) {
        return new File(String.format("./backups/%s.json", eventId));
    }

    /**
     * switch back to main Screen
     * @param actionEvent when button is pressed, go back to the main screen
     */
    public void goBackToHomeScreen(ActionEvent actionEvent) {
        mainCtrl.switchBackToMainScreen();
    }

    /**
     * switch to the delete event screen
     * @param mouseEvent when button is pressed, go to the delete event screen
     */
    public void goToDeleteEventsScreen(MouseEvent mouseEvent) {
        mainCtrl.switchToDeleteEventsScreen();
    }
}
