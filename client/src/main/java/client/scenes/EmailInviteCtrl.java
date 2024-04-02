package client.scenes;

import client.utils.ConfigUtils;
import client.utils.EmailHandler;
import client.utils.ServerUtils;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EmailInviteCtrl implements Initializable, SimpleRefreshable {
    @FXML
    private Label titleLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button inviteButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label nameFeedbackLabel;
    @FXML
    private Label emailFeedbackLabel;
    private Translation translation;
    private ServerUtils server;
    private MainCtrl mainCtrl;
    private EmailHandler emailHandler;
    private Event event;
    private ConfigUtils configUtils;

    /**
     * Constructor
     * @param translation the translation to use
     * @param server the server to use
     * @param mainCtrl the main controller
     * @param emailHandler the email handler to use
     */
    @Inject
    public EmailInviteCtrl(Translation translation, ServerUtils server, MainCtrl mainCtrl, EmailHandler emailHandler, ConfigUtils configUtils) {
        this.translation = translation;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.emailHandler = emailHandler;
        this.configUtils = configUtils;
    }

    /**
     * Initialize basic features for the Email Invite Screen
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.textProperty().bind(translation.getStringBinding("Email.TitleLabel"));
        nameLabel.textProperty().bind(translation.getStringBinding("Email.NameLabel"));
        nameTextField.promptTextProperty().bind(translation.getStringBinding("Email.NameTextField"));
        emailTextField.promptTextProperty().bind(translation.getStringBinding("Email.EmailTextField"));
        nameFeedbackLabel.textProperty().bind(translation.getStringBinding("Empty"));
        emailFeedbackLabel.textProperty().bind(translation.getStringBinding("Empty"));
        cancelButton.textProperty().bind(translation.getStringBinding("Email.CancelButton"));
        inviteButton.textProperty().bind(translation.getStringBinding("Email.InviteButton"));
    }

    public void sendInvite() {
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        Participant participant = new Participant(name);

        if (name.isEmpty()) {
            nameFeedbackLabel.textProperty().bind(translation.getStringBinding("Email.NameFeedbackLabel"));
        } else {
            nameFeedbackLabel.textProperty().bind(translation.getStringBinding("Empty"));
        }
        if (email.isEmpty()) {
            emailFeedbackLabel.textProperty().bind(translation.getStringBinding("Email.EmailFeedbackLabel"));
        } else {
            emailFeedbackLabel.textProperty().bind(translation.getStringBinding("Empty"));
        }
        if (!name.isEmpty() && !email.isEmpty()) {
            Thread emailThread = new Thread(() -> {
                boolean result = emailHandler.sendEmail(email, "Invited to splitty!", getInviteText());
                if (result){
                    Platform.runLater(() -> System.out.println("Successfully sent email!"));
                }else{
                    Platform.runLater(() -> System.out.println("Error while sending email!"));
                }
            });
            emailThread.start();
            mainCtrl.switchToEventScreen();
        }
    }

    public void cancel(){
        mainCtrl.switchToEventScreen();
    }

    public String getInviteText(){
        StringBuilder sb = new StringBuilder("You have been invited to event ");
        sb.append(event.getTitle());
        sb.append(" with the invitation code of ");
        sb.append(event.getId());
        sb.append("!");
        String serverURL = this.configUtils.easyLoadProperties().getProperty("connection.URL");
        sb.append(" The event is hosted on the server with address: ");
        sb.append(serverURL);
        return sb.toString();
    }

    @Override
    public void refresh(Event event) {
        this.event = event;
    }

    @Override
    public boolean shouldLiveRefresh() {
        return false;
    }
}
