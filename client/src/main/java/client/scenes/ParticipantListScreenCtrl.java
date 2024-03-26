package client.scenes;
import client.utils.ServerUtils;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import client.utils.Styling;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import static javafx.geometry.Pos.CENTER_LEFT;

public class ParticipantListScreenCtrl implements Initializable, SimpleRefreshable {
    private Event event;
    @FXML
    public Button goBack;
    @FXML
    private ListView<HBox> participantList;
    ServerUtils server;
    MainCtrl mainCtrl;
    Translation translation;
    private Map<Long, HBox> map;

    /**
     * constructor for the participant list screen
     */
    @Inject
    public ParticipantListScreenCtrl(MainCtrl mainCtrl, ServerUtils server, Translation translation) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        this.map = new HashMap<>();
    }
    /**
     * updates the event instance
     * @param event -> new details
     */
    public void refresh(Event event) {
        this.event = event;
        showParticipantList();
    }

    @Override
    public boolean shouldLiveRefresh() {
        return true;
    }

    /**
     * initializer for the go back button
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            Image image = new Image(new FileInputStream("client/src/main/resources/images/goBack.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setPreserveRatio(true);
            goBack.setGraphic(imageView);
            Styling.applyStyling(goBack, "positiveButton");
        } catch (FileNotFoundException e) {
            System.out.println("didn't work");
            throw new RuntimeException(e);
        }
    }

    /**
     * generates the participants in the list in the final form
     */
    public void showParticipantList () {
        participantList.getItems().clear();
        for(Participant participant: event.getParticipants()) {
            HBox participantB1 = generateParticipantBox(participant.getId(), participant.getName());
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            HBox participantBox = new HBox(generateRemoveButton(participant.getId()), participantB1, region);
            participantBox.setAlignment(Pos.CENTER_RIGHT);
            participantBox.setStyle("-fx-border-style: none;");
            participantBox.setSpacing(10);
            map.put(participant.getId(), participantBox);
            participantBox.setAlignment(CENTER_LEFT);
            participantList.getItems().add(participantBox);
        }
    }

    /**
     * generates the remove button for each participant
     * @param participantId the participant for which the remove button is being generated
     * @return returns the symbol
     */
    public ImageView generateRemoveButton(long participantId) {
        FileInputStream input;
        try {
            input = new FileInputStream("client/src/main/resources/images/x_remove.png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        int imgSize = 15;
        imageView.setFitHeight(imgSize);
        imageView.setFitWidth(imgSize);
        imageView.setPickOnBounds(true);
        imageView.setOnMouseEntered(mouseEvent -> {
            mainCtrl.getParticipantScene().
                    setCursor(Cursor.HAND);
        });
        imageView.setOnMouseExited(mouseEvent -> {
            mainCtrl.getParticipantScene().
                    setCursor(Cursor.DEFAULT);
        });
        imageView.setOnMouseClicked(mouseEvent -> {
            removeFromList(participantId);
        });
        return imageView;
    }

    /**
     * generates label for each participant
     * clicking the participant leads to the add/edit screen of the participant
     * @param participantId id of the participant
     * @param name name of the participant
     */
    public HBox generateParticipantBox(long participantId, String name) {
        HBox hbox = new HBox();
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Label participant = new Label(name);
        hbox.getChildren().addAll(participant, region);
        hbox.setOnMouseEntered(mouseEvent -> {
            mainCtrl.getParticipantScene()
                    .setCursor(Cursor.HAND);
        });
        hbox.setOnMouseExited(mouseEvent -> {
            mainCtrl.getParticipantScene()
                    .setCursor(Cursor.DEFAULT);
        });
        hbox.setOnMouseClicked(mouseEvent -> {
            mainCtrl.switchToEditParticipant(participantId);
        });
        return hbox;
    }

    /**
     * go back method when the button is clicked
     */
    public void goBack(javafx.event.ActionEvent actionEvent) {
        mainCtrl.switchToEventScreen();
    }

    /**
     * removes the participant from the list if deleted
     */
    public void removeFromList(long participantId){
        server.removeParticipant(event.getId(), participantId);
        HBox hBox = map.get(participantId);
        map.remove(participantId);
        participantList.getItems().remove(hBox);
    }

}


