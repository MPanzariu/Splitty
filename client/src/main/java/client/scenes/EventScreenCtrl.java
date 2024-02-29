package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class EventScreenCtrl implements Initializable{
    @FXML
    private Button sendInvites;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label participantsLabel;
    @FXML
    private Label participantsName;
    @FXML
    private Button editParticipant;
    @FXML
    private Button addParticipant;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    private Event event;

    @Inject
    public EventScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sendInvites.textProperty().bind(translation.getStringBinding("Event.Button.SendInvites"));
        participantsName.textProperty().bind(translation.getStringBinding("Participants.DisplayName.EventScreen"));
        try{
            Image image = new Image(new FileInputStream("client/src/main/resources/images/editing.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setPreserveRatio(true);
            editParticipant.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            System.out.println("didn't work");
            throw new RuntimeException(e);
        }
        try{
            Image image = new Image(new FileInputStream("client/src/main/resources/images/add-participant.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setPreserveRatio(true);
            addParticipant.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            System.out.println("didn't work");
            throw new RuntimeException(e);
        }
    }
    public void inviteParticipants(){
        //TO DO, implement UI for inviting participants
        System.out.println(event.toString());
    }

    public void editCurrentParticipants(){
        //TO DO, implement UI for editing exisiting participants
        System.out.println(event.toString());
    }

    public void addParticipants(){
        //TO DO, implement UI for adding participants
        System.out.println(event.toString());
    }

    public void setEvent(Event event) {
        this.event = event;
        eventNameLabel.setText(event.getTitle());
    }

    public void setParticipants(Event event){
        this.event = event;
        String participantsText = "";
        Set<Participant> participants = event.getParticipants();
        Iterator<Participant> participantIterator = participants.iterator();
        while(participantIterator.hasNext()){
            Participant current = participantIterator.next();
            if(participantIterator.hasNext())participantsText += current.getName() + ", ";
            else participantsText += current.getName();
        }
        if(participantsText.equals(""))participantsLabel.setText("There are no participants");
        else participantsLabel.setText(participantsText);
    }

    
}
