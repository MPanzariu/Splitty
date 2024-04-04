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
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class ParticipantScreenCtrl implements Initializable, SimpleRefreshable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    @FXML
    private Button cancelButton;
    @FXML
    private Label title;
    @FXML
    private Label name;
    @FXML
    private TextField nameField;
    @FXML
    private Label email;
    @FXML
    private TextField emailField;
    @FXML
    private Label iban;
    @FXML
    private TextField ibanField;
    @FXML
    private Label bic;
    @FXML
    private TextField bicField;
    @FXML
    private Button okButton;
    @FXML
    private Label bankDetails;
    @FXML
    private Label holder;
    @FXML
    private TextField holderField;

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

    /***
     * binder for the buttons, fields, labels
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources) {
        title.textProperty().bind(translation.getStringBinding("Participants.Label.title"));
        cancelButton.textProperty().bind(translation.getStringBinding("Participants.Button.cancel"));
        okButton.textProperty().bind(translation.getStringBinding("Participants.Button.ok"));
        bankDetails.textProperty().bind(translation.getStringBinding("Participants.Label.bankDetails"));
        emailField.promptTextProperty().bind(translation.getStringBinding("Participants.Field.email"));
        holderField.promptTextProperty().bind(translation.getStringBinding("Participants.Field.holder"));
        ibanField.promptTextProperty().bind(translation.getStringBinding("Participants.Field.iban"));
        nameField.promptTextProperty().bind(translation.getStringBinding("Participants.Field.name"));
        bicField.promptTextProperty().bind(translation.getStringBinding("Participants.Field.bic"));
        name.textProperty().bind(translation.getStringBinding("Participants.Label.name"));
        email.textProperty().bind(translation.getStringBinding("Participants.Label.email"));
        holder.textProperty().bind(translation.getStringBinding("Participants.Label.holder"));
        iban.textProperty().bind(translation.getStringBinding("Participants.Label.iban"));
        bic.textProperty().bind(translation.getStringBinding("Participants.Label.bic"));
    }

    /**
     * method for clicking 'ok' in add/edit participant screen
     * if the participant is new it is added to the event
     * otherwise edits the participant
     */
    public void confirmEdit() {
        Participant participant = addParticipant();
        clearFields();
        if(participantId == 0){
            server.addParticipant(event.getId(), participant);
            mainCtrl.switchScreens(EventScreenCtrl.class);
        }
        else {
            server.editParticipant(event.getId(), participantId, participant);
            participantId = 0;
            mainCtrl.switchScreens(ParticipantListScreenCtrl.class);
        }
    }

    /**
     * takes you back to the event screen if 'abort' is clicked
     */
    public void cancel() {
        clearFields();
        mainCtrl.switchScreens(EventScreenCtrl.class);
    }

    /***
     * Removes text from all details fields
     */
    public void clearFields(){
        nameField.clear();
        holderField.clear();
        ibanField.clear();
        bicField.clear();
        emailField.clear();
    }

    /**
     * creates an instance of a participant which is being assigned/updated
     * in the confirm method
     * @return returns the new participant
     */
    public Participant addParticipant(){
        String name = nameField.getText();
        String email = emailField.getText();
        String accountHolder = holderField.getText();
        String iban = ibanField.getText();
        String bic = bicField.getText();

        Participant participant = new Participant(name);
        participant.setLegalName(accountHolder);
        participant.setIban(iban);
        participant.setBic(bic);
        participant.setEmail(email);
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
        if(participantFin == null) return;
        nameField.setText(participantFin.getName());
        holderField.setText(participantFin.getLegalName());
        ibanField.setText(participantFin.getIban());
        bicField.setText(participantFin.getBic());
        emailField.setText(participantFin.getEmail());
        participantId = id;
    }

    /**
     * updates the event instance
     * @param event -> new details
     */
    public void refresh(Event event){
        this.event = event;
    }
}
