package client.scenes;

import client.utils.ManagementOverviewUtils;
import client.utils.ServerUtils;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteEventsScreenCtrl implements Initializable {
    @FXML
    private Label selectWhichEventsToDelete;
    @FXML
    private ListView<Event> checkEventsListView;
    @FXML
    private Button deleteSelectedEvenetsButton;
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
                    setGraphic(checkBox);
                }
            }
        });
    }
}
