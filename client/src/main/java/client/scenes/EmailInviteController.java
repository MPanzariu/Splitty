package client.scenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EmailInviteController implements Initializable {
    @FXML
    public Label titleLabel;
    @FXML
    public Label nameLabel;
    @FXML
    public TextField nameTextField;
    @FXML
    public Label emailLabel;
    @FXML
    public TextField emailTextField;
    @FXML
    public Button inviteButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Label nameFeedbackLabel;
    @FXML
    public Label emailFeedbackLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void sendInvite() {

    }

    public void cancel(){

    }
}
