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
     * Setter for eventTitleTextBox, it is used only for testing purposes
     * @param eventTitleTextBox the value to set it to
     */
    public void setEventTitleTextBox(TextField eventTitleTextBox) {
        this.eventTitleTextBox = eventTitleTextBox;
    }
    /**
     * Setter for inviteCodeTextBox, it is used only for testing purposes
     * @param inviteCodeTextBox the value to set it to
     */
    public void setInviteCodeTextBox(TextField inviteCodeTextBox) {
        this.inviteCodeTextBox = inviteCodeTextBox;
    }
    /**
     * Setter for createEventFeedback, it is used only for testing purposes
     * @param createEventFeedback the value to set it to
     */
    public void setCreateEventFeedback(Label createEventFeedback) {
        this.createEventFeedback = createEventFeedback;
    }
    /**
     * Setter for joinEventFeedback, it is used only for testing purposes
     * @param joinEventFeedback the value to set it to
     */
    public void setJoinEventFeedback(Label joinEventFeedback) {
        this.joinEventFeedback = joinEventFeedback;
    }

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
        joinEventFeedback.setText("");
        String inviteCode = inviteCodeTextBox.getText();
        String errorMsg = "Invalid invitation code!";
        if (inviteCode.length() != 6){
            joinEventFeedback.setText(errorMsg);
            return;
        }
        try{
            Event event = server.getEvent(inviteCodeTextBox.getText());
            mainCtrl.joinEvent(event);
            //Build fails when I use BadRequest exception
        }catch (Exception exception){
            joinEventFeedback.setText(errorMsg);
        }
    }
    /**
     * Creates and joins the event specified by the user in the text box
     */
    public void createEvent(){
        createEventFeedback.setText("");
        String title = eventTitleTextBox.getText();
        if (title.isEmpty()){
            createEventFeedback.setText("Please specify the title!");
            return;
        }
        Event event = server.createEvent(title);
        mainCtrl.joinEvent(event);
    }
}
