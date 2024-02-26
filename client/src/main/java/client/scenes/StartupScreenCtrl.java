package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class StartupScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField eventTitleTextBox;
    @FXML
    private TextField inviteCodeTextBox;
    @FXML
    private Label createEventFeedback;
    @FXML
    private Label joinEventFeedback;

    /**
     * Constructor
     * @param server the ServerUtils instance
     * @param mainCtrl the MainCtrl instance
     */
    @Inject
    public StartupScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Joins the event specified by the user in the text box
     */
    public void joinEvent(){
        String inviteCode = inviteCodeTextBox.getText();
        if (inviteCode.length() != 6){
            joinEventFeedback.setText("Invalid invite code!");
        }
        Event event = server.getEvent(inviteCodeTextBox.getText());
        mainCtrl.joinEvent(event);
    }
    /**
     * Creates and joins the event specified by the user in the text box
     */
    public void createEvent(){
        String title = eventTitleTextBox.getText();
        if (title.isEmpty()){
            createEventFeedback.setText("Please specify the title!");
            return;
        }
        Event event = server.createEvent(title);
        mainCtrl.joinEvent(event);
    }
}
