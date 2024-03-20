package client.scenes;

import client.utils.ManagementOverviewUtils;
import client.utils.ServerUtils;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DeleteEventsScreenCtrl implements Initializable {
    @FXML
    private Label selectWhichEventsToDelete;
    @FXML
    private ListView<Event> checkEventsListView;
    @FXML
    private Button deleteSelectedEvenetsButton;
    @FXML
    private Label noEventsSelectedLabel;
    @FXML
    private Button deleteAllEventsButton;
    @FXML
    private Button goBackButton;
    private Map<Event, Boolean> eventSelectionMap = new HashMap<>();
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
    public DeleteEventsScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation,
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
        selectWhichEventsToDelete.textProperty().bind(translation.getStringBinding("DES.Select.Delete"));
        deleteSelectedEvenetsButton.textProperty().bind(translation.getStringBinding("DES.Delete.Selected.Events.Button"));
        deleteAllEventsButton.textProperty().bind(translation.getStringBinding("DES.Delete.All.Events"));
        try{
            Image image = new Image(new FileInputStream("client/src/main/resources/images/goBack.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setPreserveRatio(true);
            goBackButton.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            System.out.println("didn't work");
            throw new RuntimeException(e);
        }
    }

    public void initializeEventsCheckList() {
        checkEventsListView.setItems(utils.retrieveEvents());
        checkEventsListView.setCellFactory(listView -> new ListCell<>() {
            private final CheckBox checkBox = new CheckBox();
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                if (empty || event == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    checkBox.setText("Title: " + event.getTitle() + ", ID: " + event.getId());
                    checkBox.setOnAction(e -> eventSelectionMap.put(event, checkBox.isSelected()));
                    if (!eventSelectionMap.containsKey(event)) {
                        eventSelectionMap.put(event, false);
                    }
                    checkBox.setSelected(eventSelectionMap.get(event));
                    setGraphic(checkBox);
                }
            }
        });
    }

    public void deleteSelectedEvents(ActionEvent actionEvent) {
        if(!eventSelectionMap.containsValue(true)){
            noEventsSelectedLabel.textProperty().bind(translation.getStringBinding("DES.No.Events.Selected.Label"));
            System.out.println("No events selected");
        }
        else {
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Delete Confirmation");
            confirmationDialog.setHeaderText("Delete Selected Events");
            confirmationDialog.setContentText("Are you sure you want to delete the selected events?");
            ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
            confirmationDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
            Optional<ButtonType> result = confirmationDialog.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                List<Event> selectedEvents = eventSelectionMap.entrySet().stream()
                        .filter(Map.Entry::getValue)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
                checkEventsListView.getItems().removeAll(selectedEvents);
                for (int i = 0; i < selectedEvents.size(); i++) {
                    Event current = selectedEvents.get(i);
                    server.deleteEvent(current.getId());
                    System.out.println("The following event has been deleted: " + current.getTitle());
                }
                noEventsSelectedLabel.textProperty().bind(translation.getStringBinding("DES.Event.Deleted.Sucessfully"));
                eventSelectionMap.clear();
            } else {
                noEventsSelectedLabel.textProperty().bind(translation.getStringBinding("DES.Event.Deletion.Cancel"));
                System.out.println("Deletion cancelled.");
            }
        }
    }

    public void deleteAllEvents(ActionEvent actionEvent) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Delete All Confirmation");
        confirmationDialog.setHeaderText("Delete All Events");
        confirmationDialog.setContentText("Are you sure you want to delete all the events?");
        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        confirmationDialog.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            server.deleteAllEvents();
            checkEventsListView.getItems().clear();
            eventSelectionMap.clear();
            System.out.println("Everything was deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    public void goBackToManagementOverview(ActionEvent actionEvent) {
        mainCtrl.switchToManagementOverviewScreen();
    }
}
