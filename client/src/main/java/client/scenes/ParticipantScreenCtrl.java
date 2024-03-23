package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class ParticipantScreenCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    @FXML
    public Button abortButton;
    @FXML
    public Label title;
    @FXML
    public Label name;
    @FXML
    public TextField nameField;
    @FXML
    public Label email;
    @FXML
    public TextField emailField;
    @FXML
    public Label iban;
    @FXML
    public TextField ibanField;
    @FXML
    public Label bic;
    @FXML
    public TextField bicField;
    @FXML
    public Button okButton;

    private Event event;
    private long participantId;


    @Inject
    public ParticipantScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
    }

    public void initialize(URL location, ResourceBundle resources) {
        title.textProperty().bind(translation.getStringBinding("Participants.Label.title"));
        abortButton.textProperty().bind(translation.getStringBinding("Participants.Button.abort"));
        okButton.textProperty().bind(translation.getStringBinding("Participants.Button.ok"));
        emailField.promptTextProperty().bind(translation.getStringBinding("Participants.Field.email"));
        ibanField.promptTextProperty().bind(translation.getStringBinding("Participants.Field.iban"));
        nameField.promptTextProperty().bind(translation.getStringBinding("Participants.Field.name"));
        bicField.promptTextProperty().bind(translation.getStringBinding("Participants.Field.bic"));
        name.textProperty().bind(translation.getStringBinding("Participants.Label.name"));
        email.textProperty().bind(translation.getStringBinding("Participants.Label.email"));
        iban.textProperty().bind(translation.getStringBinding("Participants.Label.iban"));
        bic.textProperty().bind(translation.getStringBinding("Participants.Label.bic"));
    }

    public void confirmEdit(ActionEvent actionEvent) {
        Participant participant = addParticipant();
        System.out.println(participantId);
        if(participantId == 0){
            server.addParticipant(event.getId(), participant.getName());
            mainCtrl.switchToEventScreen();
        }
        else {
            server.editParticipant(event.getId(), participantId, participant);
            participantId = 0;
            mainCtrl.switchToParticipantListScreen();
        }
    }

    public void cancel(ActionEvent actionEvent) {
        mainCtrl.switchToEventScreen();
    }

    public Participant addParticipant(){
        String name = nameField.getText();
        String email = emailField.getText();
        String iban = ibanField.getText();
        try {
            int bic = Integer.parseInt(String.valueOf(bicField.getText()));
        }
        catch (IllegalArgumentException e) {
            System.out.println(":<");
        }
        Participant participant = new Participant(name);
        //remember email iban bic when available
        return participant;
    }

        public void setParticipant(long id) {
            Set<Participant> participantList = event.getParticipants();
            Participant participantFin = null;
            for(Participant participant: participantList){
                if(participant.getId() == id) {
                    participantFin = participant;
                    break;
                }
            }
            if(participantFin == null)
                return;
            nameField.setText(participantFin.getName());
            participantId = id;
        }

    public void refresh(Event event){
        this.event = event;
    }
    }
