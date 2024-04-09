package client.scenes;

import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class GenerateLanguageTemplate implements Initializable, SimpleRefreshable {
    @FXML
    private Label header;
    @FXML
    private Label nameError;
    @FXML
    private TextField nameTextField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;
    private final Translation translation;
    private final MainCtrl ctrl;

    @Inject
    public GenerateLanguageTemplate(Translation translation, MainCtrl ctrl) {
        this.translation = translation;
        this.ctrl = ctrl;
    }

    @Override
    public void initialize(URL url, ResourceBundle rsc) {
        header.textProperty().bind(translation.getStringBinding("Language.header"));
        nameError.textProperty().bind(translation.getStringBinding("Language.nameError"));
        nameTextField.textProperty().bind(translation.getStringBinding("Language.nameTextField"));
        cancelButton.textProperty().bind(translation.getStringBinding("Language.cancelButton"));
        confirmButton.textProperty().bind(translation.getStringBinding("Language.confirmButton"));
    }

    @Override
    public void refresh(Event event) {

    }

    public void cancel() {
        ctrl.switchScreens(EventScreenCtrl.class);
    }
}
