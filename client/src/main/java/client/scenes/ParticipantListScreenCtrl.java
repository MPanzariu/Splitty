package client.scenes;
import client.utils.ServerUtils;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.servlet.http.Part;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import static javafx.geometry.Pos.CENTER_LEFT;

public class ParticipantListScreenCtrl implements Initializable {
    private Event event;
    @FXML
    public Button goBack;
    @FXML
    private ListView<HBox> participantList;
    ServerUtils server;
    MainCtrl mainCtrl;
    Translation translation;
    private Map<Long, HBox> map;

    @Inject
    public ParticipantListScreenCtrl(MainCtrl mainCtrl, ServerUtils server, Translation translation) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        this.map = new HashMap<>();
    }

    public void refresh(Event event) {
        this.event = event;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            Image image = new Image(new FileInputStream("client/src/main/resources/images/goBack.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setPreserveRatio(true);
            goBack.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            System.out.println("didn't work");
            throw new RuntimeException(e);
        }
    }

    public void showParticipantList () throws FileNotFoundException {
        participantList.getItems().clear();
        for(Participant participant: event.getParticipants()) {
            Label expenseText = generateParticipantLabel(participant.getId(), participant.getName());
            HBox participantBox = new HBox(generateRemoveButton(participant.getId()), expenseText);
            participantBox.setStyle("-fx-border-style: none;");
            participantBox.setSpacing(10);
            map.put(participant.getId(), participantBox);
            participantBox.setAlignment(CENTER_LEFT);
            participantList.getItems().add(participantBox);
        }
    }

    public ImageView generateRemoveButton (long participantId) throws FileNotFoundException {
        FileInputStream input = null;
        input = new FileInputStream("client/src/main/resources/images/x_remove.png");
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
            try {
                removeFromList(participantId);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return imageView;
    }

    public Label generateParticipantLabel(long participantId, String name) {
        Label participant = new Label(name);
        participant.setOnMouseEntered(mouseEvent -> {
            mainCtrl.getParticipantScene()
                    .setCursor(Cursor.HAND);
        });
        participant.setOnMouseExited(mouseEvent -> {
            mainCtrl.getParticipantScene()
                    .setCursor(Cursor.DEFAULT);
        });
        participant.setOnMouseClicked(mouseEvent -> {
            mainCtrl.switchToEditParticipant(participantId);
        });
        return participant;
    }

    public void goBack(javafx.event.ActionEvent actionEvent) {
        mainCtrl.switchToEventScreen();
    }

    public void removeFromList(long participantId) throws FileNotFoundException {
        server.removeParticipant(event.getId(), participantId);
        HBox hBox = map.get(participantId);
        map.remove(participantId);
        participantList.getItems().remove(hBox);
    }

}


