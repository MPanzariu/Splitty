package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagementOverviewPasswordCtrl implements Initializable {
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label inputPasswordLabel;
    @FXML
    private Button logInButton;
    @FXML
    private Label logInFeedback;
    @FXML
    private Button goBackToMainScreen;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    @Inject
    public ManagementOverviewPasswordCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordField.promptTextProperty().bind(translation.getStringBinding("MOPCtrl.Password.Field"));
        inputPasswordLabel.textProperty().bind(translation.getStringBinding("MOPCtrl.Input.Password.Label"));
        logInButton.textProperty().bind(translation.getStringBinding("MOPCtrl.Log.In.Button"));
        try{
            Image image = new Image(new FileInputStream("client/src/main/resources/images/goBack.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setPreserveRatio(true);
            goBackToMainScreen.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            System.out.println("didn't work");
            throw new RuntimeException(e);
        }
    }

    public void logInCheck(ActionEvent actionEvent) {
        String inputPassword = passwordField.getText();
        if(inputPassword == null || inputPassword.isEmpty() || !server.checkPassword(inputPassword)){
            logInFeedback.textProperty().bind(translation.getStringBinding("MOPCtrl.Log.In.Feedback"));
            passwordField.clear();
        }
        else{
            System.out.println("To be implemented");
        }
    }

    public void goBackToMain(ActionEvent actionEvent) {
        mainCtrl.switchBackToMainScreen();
    }
}
