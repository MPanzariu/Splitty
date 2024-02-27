package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

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
    @FXML
    private VBox recentlyViewedEventsVBox;

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
    public void joinEventClicked(){
        joinEventFeedback.setText("");
        String inviteCode = inviteCodeTextBox.getText();
        String errorMsg = "Invalid invitation code!";
        if (inviteCode.length() != 6){
            joinEventFeedback.setText(errorMsg);
            return;
        }
        try{
            Event event = server.getEvent(inviteCodeTextBox.getText());
            joinEvent(event);
            //Build fails when I use BadRequest exception
        }catch (Exception exception){
            joinEventFeedback.setText(errorMsg);
        }
    }

    public void joinEvent(Event event){
        mainCtrl.joinEvent(event);
        Label eventLabel = generateLabelForEvent(event);
        recentlyViewedEventsVBox.getChildren().add(eventLabel);
    }

    public Label generateLabelForEvent(Event event){
        String eventTitle = event.getTitle();
        Label label = new Label();
        label.setText(eventTitle);
        label.setOnMouseClicked(
                mouseEvent -> {
                    joinEvent(event);
                    removeFromVBox(label.idProperty());
                    System.out.println("clicked!");
                });
        return label;
    }

    public void removeFromVBox(StringProperty id){
        List<Node> allNodes = recentlyViewedEventsVBox.getChildren();
        Node removeNode = null;
        for (Node node : allNodes){
            if (node.idProperty().equals(id)){
                removeNode = node;
            }
        }
        if (removeNode != null){
            allNodes.remove(removeNode);
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
        System.out.println(event);
        mainCtrl.joinEvent(event);
    }
}
