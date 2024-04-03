package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import commons.Event;
import commons.Tag;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTagCtrl implements Initializable, SimpleRefreshable {
    @FXML
    private Label createTagLabel;
    @FXML
    private Label tagNameLabel;
    @FXML
    private Label errorMessageTagName;
    @FXML
    private Label colorCodeLabel;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;
    @FXML
    private TextField tagNameTextField;
    private Event event;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    private final LanguageIndicatorCtrl languageCtrl;

    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation,
                      LanguageIndicatorCtrl languageCtrl){
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        this.languageCtrl = languageCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createTagLabel.textProperty()
                .bind(translation.getStringBinding("AddTag.Label.CreateNewTag"));
        tagNameLabel.textProperty()
                .bind(translation.getStringBinding("AddTag.Label.TagName"));
        colorCodeLabel.textProperty()
                .bind(translation.getStringBinding("AddTag.Label.SelectColor"));
        cancelButton.textProperty()
                .bind(translation.getStringBinding("AddTag.Button.Cancel"));
        confirmButton.textProperty()
                .bind(translation.getStringBinding("AddTag.Button.Confirm"));
    }

    @Override
    public void refresh(Event event) {
        this.event = event;
    }
    public void onCancel() {
        errorMessageTagName.textProperty()
                .bind(translation.getStringBinding("empty"));

    }

    public void onConfirm(){
        String tagname = tagNameTextField.getText();
        if(tagname.isEmpty()){
            errorMessageTagName.textProperty()
                    .bind(translation.getStringBinding("AddTag.Label.TagError"));
        }
        else {
            errorMessageTagName.textProperty()
                    .bind(translation.getStringBinding("empty"));
            Color selectedColor = colorPicker.getValue();
            String colorCode = String.format("#%02X%02X%02X",
                    (int) (selectedColor.getRed() * 255),
                    (int) (selectedColor.getGreen() * 255),
                    (int) (selectedColor.getBlue() * 255));
            Tag newTag = new Tag(tagname, colorCode);
            System.out.println(event.getTitle());
        }
    }
}
