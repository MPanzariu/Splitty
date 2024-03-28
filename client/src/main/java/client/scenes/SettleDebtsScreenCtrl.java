package client.scenes;

import client.utils.ImageUtils;
import client.utils.SettleDebtsUtils;
import client.utils.Styling;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static javafx.geometry.Pos.CENTER_LEFT;

public class SettleDebtsScreenCtrl implements Initializable, SimpleRefreshable {
    private final MainCtrl mainCtrl;
    private final Translation translation;
    private final SettleDebtsUtils utils;
    @FXML
    private Label settleDebtsLabel;
    @FXML
    private Button goBackButton;
    @FXML
    private VBox settleVBox;
    private Event event;
    private Pair<Pane, Button> lastExpanded;

    /***
     * Constructor for the SettleDebtsScreen
     * @param mainCtrl the Main controller to use
     * @param translation the translation to use
     * @param utils the server utilities to use
     */
    @Inject
    public SettleDebtsScreenCtrl(MainCtrl mainCtrl,
                                 Translation translation, SettleDebtsUtils utils) {
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        this.utils = utils;
        this.event = null;
        this.lastExpanded = null;
    }

    /***
     * Initializes the controller
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
        settleDebtsLabel.textProperty()
                .bind(translation.getStringBinding("SettleDebts.Label.title"));
        var backImage = ImageUtils.loadImageFile("goBack.png");
        var backImageView = ImageUtils.generateImageView(backImage, 20);
        goBackButton.setGraphic(backImageView);
    }

    /***
     * Populates the VBox with all the debts
     */
    public void populateVBox(){
        List<Node> children = settleVBox.getChildren();
        children.clear();
        var owedShares = event.getOwedShares();

        Image expandButtonImage = ImageUtils.loadImageFile("singlearrow.png");

        for(Map.Entry<Participant, Integer> entry: owedShares.entrySet()){
            Participant participantOwes = entry.getKey();
            int amount = entry.getValue();

            /*
            In the current implementation, as per the basic requirements,
            there are no "debts" to anyone, only amounts owed to the group.
            This is added for extensibility later, as are all the methods that use it,
            so that transfer instructions (A sends X to B) can be generated easily
             */
            Participant participantOwedTo = participantOwes; // requisite feature not implemented

            Label debtLabel = generateDebtLabel(participantOwes, amount, participantOwedTo);
            TextArea participantText = generateParticipantText(participantOwedTo);
            Pane bankDetailsPane = generateBankDetailsPane(participantText);

            ImageView expandButtonInnerImage = ImageUtils.generateImageView(expandButtonImage, 25);
            Button expandButton = generateExpandButton(bankDetailsPane, expandButtonInnerImage);
            Button settleButton = generateSettleButton(participantOwes, amount, participantOwedTo);
            HBox debtBox = generateDebtBox(expandButton, debtLabel, settleButton);
            children.addLast(debtBox);
            children.addLast(bankDetailsPane);
        }

    }

    /***
     * Generates the expandable pane with payment details
     * @param text the TextArea containing payment instructions
     * @return a VBox Pane encapsulating payment details
     */
    public Pane generateBankDetailsPane(TextArea text) {
        VBox pane = new VBox();
        pane.getChildren().add(text);
        pane.setVisible(false);
        pane.setManaged(false);
        Styling.applyStyling(pane, "borderVBox");
        pane.setMaxWidth(400);
        return pane;
    }

    /***
     * Generates a TextArea containing bank details for a given Participant
     * @param participant the Participant to extract bank details from
     * @return a TextArea with all the Participant's bank information
     */
    public TextArea generateParticipantText(Participant participant){
        TextArea text = new TextArea(utils.getBankDetails(participant));
        text.setEditable(false);
        text.setPrefRowCount(4);
        text.setFont(new Font(14));
        Styling.applyStyling(text, "backgroundLight");
        return text;
    }

    /***
     * Generates an HBox with the Expand button, Debt information, and Settle button
     * @param expandButton the Expand Details button
     * @param debtLabel the Label detailing who owes what to who
     * @param settleButton the Mark Received button
     * @return an HBox containing all given elements, plus the correct spacing
     */
    public HBox generateDebtBox(Button expandButton, Label debtLabel, Button settleButton){
        Region spacingL = new Region();
        HBox.setHgrow(spacingL, Priority.ALWAYS);
        Region spacingR = new Region();
        HBox.setHgrow(spacingR, Priority.ALWAYS);
        HBox box = new HBox(expandButton, spacingL, debtLabel, spacingR, settleButton);
        box.setSpacing(20);
        box.setAlignment(CENTER_LEFT);
        return box;
    }

    /***
     * Generates a Button that marks a debt payment as received and settled
     * @param participantOwes the Participant owing the debt
     * @param amount the amount, in cents, to transfer
     * @param participantOwedTo the Participant who the debt is owed to
     * @return a Button that settles the debt when clicked
     */
    public Button generateSettleButton(Participant participantOwes,
                                        int amount,
                                        Participant participantOwedTo) {
        Button button = new Button();
        button.textProperty().bind(translation.getStringBinding("SettleDebts.Button.received"));
        Styling.applyStyling(button, "positiveButton");
        var onClick = utils.createSettleAction(participantOwes, amount,
                participantOwedTo, event.getId());
        button.setOnAction(onClick);

        return button;
    }

    /***
     * Generates a Button that expands the bank details pane for a debt
     * @param pane the Pane to expand/collapse
     * @param expandButtonInnerImage the image for the button, passed in here for better perforamce
     * @return a Button that expands/collapses the given Pane when clicked
     */
    public Button generateExpandButton(Pane pane, ImageView expandButtonInnerImage) {
        Button button = new Button();
        Styling.applyStyling(button, "positiveButton");
        button.setGraphic(expandButtonInnerImage);
        button.setOnMouseClicked((action)-> {
            pane.setVisible(!pane.isVisible());
            pane.setManaged(!pane.isManaged());
            if(pane.isVisible()){
                button.setRotate(90);
                if(lastExpanded!=null) lastExpanded.getValue().getOnMouseClicked().handle(null);
                lastExpanded = new Pair<>(pane, button);
            } else {
                button.setRotate(0);
                lastExpanded = null;
            }
        });
        return button;
    }

    /***
     * Generates a Label detailing who should send how much money to who
     * @param participantOwes the Participant owing the debt
     * @param amount the amount, in cents, to transfer
     * @param participantOwedTo the Participant who the debt is owed to
     * @return a Label explaining who owes how much to who
     */
    public Label generateDebtLabel(Participant participantOwes,
                                    int amount, Participant participantOwedTo) {
        Label label = new Label();
        String debtString = utils.createDebtString(participantOwes.getName(),
                amount, "NOT IMPLEMENTED");
        label.setText(debtString);
        return label;
    }

    /***
     * Replaces the Event data
     * @param event the new event data to use
     */
    public void refresh(Event event){
        this.event = event;
        populateVBox();
    }

    /***
     * Switches back to the event screen
     */
    public void switchToEventScreen() {
        mainCtrl.switchToEventScreen();
    }

    /***
     * Specifies if the screen should be live-refreshed
     * @return true if changes should immediately refresh the screen, false otherwise
     */
    @Override
    public boolean shouldLiveRefresh() {
        return true;
    }
}
