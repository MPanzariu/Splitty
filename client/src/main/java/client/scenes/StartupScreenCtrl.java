package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            if (recentlyViewedEventsVBox.getChildren().size() > 5){
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
        List<Node> nodes = recentlyViewedEventsVBox.getChildren();
        List<HBox> hBoxes = nodes.stream()
                .filter(x -> x.getClass().equals(HBox.class))
                .map(x -> (HBox) x).toList();
        for (HBox hbox : hBoxes){
            //There is only 1 label in each hbox
            Label label = hbox.getChildren()
                    .stream()
                    .filter(x -> x.getClass().equals(Label.class))
                    .map(x -> (Label) x)
                    .toList().get(0);
            if (label.getText().equals(event.getTitle())){
                removeFromVBox(hbox.idProperty());
            }
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
        label.setText(eventTitle);
        label.setOnMouseClicked(
                mouseEvent -> {
                    joinEvent(event);
                    removeFromVBox(label.getParent().idProperty());
                });
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
        createEventFeedback.setText("");
        String title = eventTitleTextBox.getText();
        if (title.isEmpty()){
            createEventFeedback.setText("Please specify the title!");
            return;
        }
        Event event = server.createEvent(title);
        System.out.println(event);
        joinEvent(event);
    }
}
