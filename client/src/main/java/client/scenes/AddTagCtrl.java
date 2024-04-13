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
import java.util.Set;

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
    private Long tagId;
    private Long expenseId;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;

    /**
     *
     * @param server the server to which the client is connected
     * @param mainCtrl the main controller
     * @param translation the class that manages translations
     */
    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation){
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
    }

    /**
     *
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

    /**
     *
     * @param event the new Event data to process
     */
    @Override
    public void refresh(Event event) {
        this.event = event;
    }

    /**
     * Fill the user inputs with the currently edited tag
     * @param tag Currently edited tag
     */
    public void fillInput(Tag tag, Long expenseId) {
        setIds(tag, expenseId);
        tagNameTextField.setText(tag.getTagName());
        tagNameTextField.selectEnd();
        tagNameTextField.deselect();
        colorPicker.setValue(Color.valueOf(tag.getColorCode()));
    }

    public void setIds(Tag tag, Long expenseId) {
        tagId = tag.getId();
        this.expenseId = expenseId;
    }

    /**
     * this is the method that runs when pressing the cancel button
     * empties the filled in fields and sets tagId to null
     */
    public void onCancel() {
        errorMessageTagName.textProperty()
                .bind(translation.getStringBinding("empty"));
        tagNameTextField.clear();
        colorPicker.setValue(Color.WHITE);
        switchScreens(null, null);
    }

    /**
     * this is the method that runs when pressing the confirm button
     * it first checks to see if the user wrote a title for the tag, else it gives an error
     * then it gets the tag color from the colorPicker.
     * If tagId is null, then a new tag is being created. Else the tag with tagId is edited.
     * Afterward all user inputs are cleared and tagId is set to null
     */
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
            Tag tag = null;
            if(tagId == null)
                server.addTagToEvent(event.getId(), tagname, colorCode);
            else
                tag = server.editTag(event.getId(), String.valueOf(tagId), new Tag(tagname, colorCode));
            tagNameTextField.clear();
            colorPicker.setValue(Color.WHITE);
            switchScreens(tag, event);
        }
    }

    /**
     * If a tag is currently being edited, then return to the expense screen with its data filled in again.
     * Else switch to the event overview.
     */
    public void switchScreens(Tag tag, Event event) {
        if(tagId != null) {
            Set<Tag> tags = event.getEventTags();
            tags.removeIf(tag1 -> tag1.getId() == tag.getId());
            tags.add(tag);
            mainCtrl.switchToEditExpense(expenseId, tags);
            expenseId = null;
        } else {
            mainCtrl.switchScreens(EventScreenCtrl.class);
        }
        tagId = null;
    }

    public Long getTagId() {
        return tagId;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
