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
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class ParticipantScreenCtrl implements Initializable, SimpleRefreshable {
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

    /**
     * constructor
     * @param server -> the server
     * @param mainCtrl main controller
     * @param translation for translating buttons and fields
     */
    @Inject
    public ParticipantScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
    }

    /**
     *binder for the buttons, fields, labels
     */
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

    /**
     * method for clicking 'ok' in add/edit participant screen
     * if the participant is new it is added to the event
     * otherwise edits the participant
     */
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

    /**
     * takes you back to the event screen if 'abort' is clicked
     */
    public void cancel(ActionEvent actionEvent) {
        mainCtrl.switchToEventScreen();
    }

    /**
     * creates an instance of a participant which is being assigned/updated
     * in the confirm method
     * @return returns the new participant
     */
    public Participant addParticipant(){
        String name = nameField.getText();
        String accountHolder = null;
        String email = emailField.getText();
        String iban = ibanField.getText();
        String bic = bicField.getText();

        Participant participant = new Participant(name);
        //remember email iban bic when available
        return participant;
    }

    /**
     * sets up the screen when editing a participant
     * -> selects details of the participant in the current state
     * @param id -> id of the participant
     */
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

    /**
     * updates the event instance
     * @param event -> new details
     */
    public void refresh(Event event){
        this.event = event;
    }

    /***
     * Specifies if the screen should be live-refreshed
     * @return true if changes should immediately refresh the screen, false otherwise
     */
    @Override
    public boolean shouldLiveRefresh() {
        return false;
    }
}
