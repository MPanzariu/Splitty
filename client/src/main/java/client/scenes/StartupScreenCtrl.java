package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.sql.rowset.spi.TransactionalWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import static javafx.geometry.Pos.CENTER_LEFT;
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
    @FXML
    private Button joinEventButton;
    @FXML
    private Button createEventButton;
    @FXML
    private Label createEventLabel;
    @FXML
    private Label joinEventLabel;

    private HashMap<Event, HBox> eventHBoxHashMap;
    private HashMap<HBox, Event> hBoxEventHashMap;

    private Translation translation;
    /**
     * Setter for eventTitleTextBox
     * @param eventTitleTextBox the value to set it to
     */
    public void setEventTitleTextBox(TextField eventTitleTextBox) {
        this.eventTitleTextBox = eventTitleTextBox;
    }
    /**
     * Setter for inviteCodeTextBox
     * @param inviteCodeTextBox the value to set it to
     */
    public void setInviteCodeTextBox(TextField inviteCodeTextBox) {
        this.inviteCodeTextBox = inviteCodeTextBox;
    }
    /**
     * Setter for createEventFeedback
     * @param createEventFeedback the value to set it to
     */
    public void setCreateEventFeedback(Label createEventFeedback) {
        this.createEventFeedback = createEventFeedback;
    }
    /**
     * Setter for joinEventFeedback
     * @param joinEventFeedback the value to set it to
     */
    public void setJoinEventFeedback(Label joinEventFeedback) {
        this.joinEventFeedback = joinEventFeedback;
    }

    /**
     * Setter for recentlyViewedEventsVBox
     * @param recentlyViewedEventsVBox the value to set it to
     */
    public void setRecentlyViewedEventsVBox(VBox recentlyViewedEventsVBox) {
        this.recentlyViewedEventsVBox = recentlyViewedEventsVBox;
    }

    /**
     * Constructor
     * @param server the ServerUtils instance
     * @param mainCtrl the MainCtrl instance
     */
    @Inject
    public StartupScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        eventHBoxHashMap = new HashMap<>();
        hBoxEventHashMap = new HashMap<>();
    }

    /**
     * Binds the fields to their matching binding
     */
    public void bindFields(){
        eventTitleTextBox.promptTextProperty().bind(translation.getStringBinding("Startup.TextBox.EventTitle"));
        inviteCodeTextBox.promptTextProperty().bind(translation.getStringBinding("Startup.TextBox.EventCode"));
        joinEventLabel.textProperty().bind(translation.getStringBinding("Startup.Label.JoinEvent"));
        createEventLabel.textProperty().bind(translation.getStringBinding("Startup.Label.CreateEvent"));
        joinEventButton.textProperty().bind(translation.getStringBinding("Startup.Button.JoinEvent"));
        createEventButton.textProperty().bind(translation.getStringBinding("Startup.Button.CreateEvent"));
        joinEventFeedback.textProperty().bind(translation.getStringBinding("empty"));
        createEventFeedback.textProperty().bind(translation.getStringBinding("empty"));
    }

    /**
     * Joins the event specified by the user in the text box
     */
    public void joinEventClicked(){
        joinEventFeedback.textProperty().bind(translation.getStringBinding("empty"));
        String inviteCode = inviteCodeTextBox.getText();
        if (inviteCode.length() != 6){
            joinEventFeedback.textProperty().bind(translation.getStringBinding("Startup.Label.InvalidCode"));
            return;
        }
        try{
            Event event = server.getEvent(inviteCodeTextBox.getText());
            joinEvent(event);
            //Build fails when I use BadRequest exception
        }catch (Exception exception){
            joinEventFeedback.textProperty().bind(translation.getStringBinding("Startup.Label.InvalidCode"));
        }
    }

    /**
     * Joins the given event
     * @param event the event to join
     */
    public void joinEvent(Event event){
        mainCtrl.joinEvent(event);
        Label eventLabel = generateLabelForEvent(event);
        try{
            ImageView imageView = generateRemoveButton(eventLabel);
            HBox hbox = generateHBox(eventLabel, imageView);
            removeFromHistoryIfExists(event);
            List<Node> recentlyViewedEvents = recentlyViewedEventsVBox.getChildren();
            recentlyViewedEvents.addFirst(hbox);
            eventHBoxHashMap.put(event, hbox);
            hBoxEventHashMap.put(hbox, event);
            if (recentlyViewedEventsVBox.getChildren().size() > 5){
                HBox lastHBox = (HBox) recentlyViewedEvents.getLast();
                Event removedEvent = hBoxEventHashMap.remove(lastHBox);
                eventHBoxHashMap.remove(removedEvent);
                hBoxEventHashMap.remove(lastHBox);
                recentlyViewedEvents.removeLast();
            }
        }catch (FileNotFoundException e){
            System.out.println("File was not found!");
        }
    }
    /**
     * Removes the HBox containing the event given if it exists already in the history
     * @param event the event to remove the history of
     */
    public void removeFromHistoryIfExists(Event event){
        if (eventHBoxHashMap.containsKey(event)){
            HBox hBox = eventHBoxHashMap.get(event);
            eventHBoxHashMap.remove(event);
            hBoxEventHashMap.remove(hBox);
            recentlyViewedEventsVBox.getChildren().remove(hBox);
        }
    }

    /**
     * Generates the label for the history
     * @param event the event to generate the label for
     * @return the label generated
     */
    public Label generateLabelForEvent(Event event){
        String eventTitle = event.getTitle();
        Label label = new Label();
        StringBuilder sb = new StringBuilder(eventTitle);
        sb.append(" (");
        sb.append(event.getId());
        sb.append(")");
        label.setText(sb.toString().toLowerCase());

        label.setOnMouseClicked(
                mouseEvent -> {
                    joinEvent(event);
                    removeFromVBox(label.getParent().idProperty());
                    joinEventFeedback.textProperty().bind(translation.getStringBinding("empty"));
                });
        label.setOnMouseEntered(
                mouseEvent -> {
                    label.setUnderline(true);
                    mainCtrl.getMainMenuScene().setCursor(Cursor.HAND);
                }
        );
        label.setOnMouseExited(
                mouseEvent -> {
                    label.setUnderline(false);
                    mainCtrl.getMainMenuScene().setCursor(Cursor.DEFAULT);
                }
        );
        return label;
    }

    /**
     * Generates the HBox for the history (which will contain the label and image)
     * @param label the label to display
     * @param imageView the image to display
     * @return HBox generated
     */

    public HBox generateHBox(Label label, ImageView imageView){
        HBox hbox = new HBox(imageView, label);
        hbox.setSpacing(10);
        hbox.setAlignment(CENTER_LEFT);
        return hbox;
    }
    /**
     * Generates an image which when clicked will remove the HBox which contains it.
     * It will also change the cursor of the user while hovering to indicate clickability.
     * @param label the label which will be contained with the image in the HBox
     * @return ImageView with the image
     */
    public ImageView generateRemoveButton(Label label) throws FileNotFoundException {
        FileInputStream input = null;
        input = new FileInputStream("src/main/resources/images/x_remove.png");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        int imgSize = 15;
        imageView.setFitHeight(imgSize);
        imageView.setFitWidth(imgSize);
        imageView.setPickOnBounds(true);
        imageView.setOnMouseEntered(
                mouseEvent -> {
                    mainCtrl.getMainMenuScene().setCursor(Cursor.HAND);
                }
        );
        imageView.setOnMouseExited(
                mouseEvent -> {
                    mainCtrl.getMainMenuScene().setCursor(Cursor.DEFAULT);
                }
        );
        imageView.setOnMouseClicked(
                mouseEvent -> {
                    removeFromVBox(label.getParent().idProperty());
                }
        );
        return imageView;
    }
    /**
     * Removes from the VBox the child with the given id
     * @param id the given id
     */
    public void removeFromVBox(StringProperty id){
        List<Node> allNodes = recentlyViewedEventsVBox.getChildren();
        Node removeNode = null;
        for (Node node : allNodes){
            if (node.idProperty() != null && node.idProperty().equals(id)){
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
        createEventFeedback.textProperty().bind(translation.getStringBinding("empty"));
        String title = eventTitleTextBox.getText();
        if (title.isEmpty()){
            createEventFeedback.textProperty().bind(translation.getStringBinding("Startup.Label.UnspecifiedTitle"));
            return;
        }
        Event event = server.createEvent(title);
        System.out.println(event);
        joinEvent(event);
    }
}