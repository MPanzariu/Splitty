package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class ManagementOverviewScreenCtrl implements Initializable {
    @FXML
    private Button homeScreenButton;
    @FXML
    private Label MOTitle;
    @FXML
    private Label eventsLabel;
    @FXML
    private Label participantsLabel;
    @FXML
    private ListView eventsListView;
    @FXML
    private ListView participantsListView;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    @Inject
    public ManagementOverviewScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MOTitle.textProperty().bind(translation.getStringBinding("MOSCtrl.Title"));
        eventsLabel.textProperty().bind(translation.getStringBinding("MOSCtrl.Events.Label"));
        participantsLabel.textProperty().bind(translation.getStringBinding("MOSCtrl.Participants.Label"));
        try{
            Image image = new Image(new FileInputStream("client/src/main/resources/images/home-page.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setPreserveRatio(true);
            homeScreenButton.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            System.out.println("didn't work");
            throw new RuntimeException(e);
        }
        refreshListView();
    }

    public void goBackToHomeScreen(ActionEvent actionEvent) {
        mainCtrl.switchBackToMainScreen();
    }

    public void refreshListView(){
        List<Event> events = server.retrieveAllEvents();
        for(int i = 0; i < events.size(); i++){
            Event current = events.get(i);
            String input = "";
            input+="Name: \"" +  current.getTitle() + "\"";
            input+=", ID: \"" + current.getId() + "\"";
            eventsListView.getItems().add(input);
        }
    }

    public void selectEvent(MouseEvent mouseEvent) {
        List<Event> events = server.retrieveAllEvents();
        participantsListView.getItems().clear();
        String input = mouseEvent.getPickResult().toString();
        Scanner sc = new Scanner(input);
        sc.useDelimiter("\"");
        sc.next();
        sc.next();
        sc.next();
        sc.next();
        String eventId = sc.next();
        Event current = null;
        for(int i = 0; i < events.size(); i++){
            current = events.get(i);
            if(current.getId().equals(eventId))break;
            else current = null;
        }
        if(current!=null){
            Set<Participant> participants = current.getParticipants();
            Iterator<Participant> participantIterator = participants.iterator();
            while(participantIterator.hasNext()){
                Participant toAdd = participantIterator.next();
                participantsListView.getItems().add(toAdd.getName());
            }
            System.out.println("it worked but no participants (yet)");
        }
        else System.out.println("No event with this ID");
    }
}
