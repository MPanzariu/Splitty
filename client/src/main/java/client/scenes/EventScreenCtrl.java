package client.scenes;

import client.utils.EmailHandler;
import client.utils.ServerUtils;
import client.utils.Styling;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

import static javafx.geometry.Pos.CENTER_LEFT;

public class EventScreenCtrl implements Initializable, SimpleRefreshable{
    @FXML
    private TextField invitationCode;
    @FXML
    private Button addExpense;
    @FXML
    private Button allExpenses;
    @FXML
    private Button fromButton;
    @FXML
    private Button inButton;
    @FXML
    private Button settleDebtsButton;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label participantsLabel;
    @FXML
    private Label participantsName;
    @FXML
    private Label expenseLabel;
    @FXML
    private Button editParticipant;
    @FXML
    private Button addParticipant;
    @FXML
    private Button goBackButton;
    @FXML
    private ComboBox<String> cBoxParticipantExpenses;
    @FXML
    private Label errorInvalidParticipant;
    @FXML
    private ListView<HBox> expensesLogListView;
    @FXML
    private HBox buttonsHBox;
    @FXML
    private ComboBox<Locale> languageIndicator;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    private final LanguageIndicatorCtrl languageCtrl;
    private Event event;
    private Map<Long, HBox> hBoxMap;
    private Button selectedExpenseListButton;
    @FXML
    private Button testEmailButton;
    private EmailHandler emailHandler;
    /**
     * Constructor
     * @param server the ServerUtils instance
     * @param mainCtrl the MainCtrl instance
     * @param translation the Translation to use
     */
    @Inject
    public EventScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation,
                           LanguageIndicatorCtrl languageCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        this.event = null;
        this.hBoxMap = new HashMap<>();
        this.buttonsHBox = new HBox();
        this.languageCtrl = languageCtrl;
        this.selectedExpenseListButton = null;
    }

    /**
     * Initialize basic features of the application, bind text to be translated,
     * set images for edit, add participants  and go back buttons
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
        invitationCode.setEditable(false);
        participantsName.textProperty().bind(translation.getStringBinding("Participants.DisplayName.EventScreen"));
        expenseLabel.textProperty().bind(translation.getStringBinding("Expense.Label.Display.EventScreen"));
        addExpense.textProperty().bind(translation.getStringBinding("Event.Button.AddExpense"));
        errorInvalidParticipant.textProperty().bind(translation.getStringBinding("empty"));
        settleDebtsButton.textProperty().bind(translation.getStringBinding("Event.Button.SettleDebts"));
        testEmailButton.textProperty().bind(translation.getStringBinding("Event.Button.TestEmail"));
        testEmailButton.setOnAction(event -> sendTestEmail());
        initializeEditTitle();
        try{
            Image image = new Image(new FileInputStream("client/src/main/resources/images/editing.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setPreserveRatio(true);
            editParticipant.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            System.out.println("didn't work");
            throw new RuntimeException(e);
        }
        try{
            Image image = new Image(new FileInputStream("client/src/main/resources/images/add-participant.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setPreserveRatio(true);
            addParticipant.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            System.out.println("didn't work");
            throw new RuntimeException(e);
        }
        try{
            Image image = new Image(new FileInputStream("client/src/main/resources/images/goBack.png"));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(15);
            imageView.setFitHeight(15);
            imageView.setPreserveRatio(true);
            goBackButton.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            System.out.println("didn't work");
            throw new RuntimeException(e);
        }
        initializeParticipantsCBox();
        languageCtrl.initializeLanguageIndicator(languageIndicator);
        emailHandler = new EmailHandler();
        if (emailHandler.isConfigured()) {
            Styling.removeStyling(testEmailButton, "disabledButton");
        }
    }

    public void initializeParticipantsCBox() {
        cBoxParticipantExpenses.valueProperty()
            .addListener((observable, oldValue, newValue) -> {
                boolean validParticipant = false;
                Set<Participant> participants = event.getParticipants();
                for(Participant participant: participants)
                    if(participant.getName().equalsIgnoreCase(newValue)){
                        validParticipant = true;
                        break;
                    }
                if(validParticipant) {
                    errorInvalidParticipant.textProperty()
                        .bind(translation.getStringBinding("empty"));
                    setButtonsNames(newValue);
                }
                else
                    errorInvalidParticipant.textProperty()
                        .bind(translation.getStringBinding("Event.Label.Error.InvalidParticipant"));
            });
    }

    private void initializeEditTitle() {
        eventNameLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2)
                    mainCtrl.openEditTitle();
            }
        });
    }

    /***
     * Updates all data viewed
     * @param event the Event to extract data from
     */
    public void refresh(Event event){
        this.event = event;
        updateEventText();
        updateParticipants();
        updateParticipantsDropdown();
        refreshExpenseList();
        errorInvalidParticipant.textProperty()
                .bind(translation.getStringBinding("empty"));
        buttonsHBox.getChildren().clear();
        languageCtrl.refresh(languageIndicator);
    }

    /***
     * Specifies if the screen should be live-refreshed
     * @return true if changes should immediately refresh the screen, false otherwise
     */
    @Override
    public boolean shouldLiveRefresh() {
        return true;
    }

    /**
     * Open the title editing screen.
     */
    public void editTitle() {
        mainCtrl.openEditTitle();
    }

    /**
     * UI for inviting participants that needs to be implemented when the button is pressed
     */
    public void inviteParticipants(){
        //TODO: implement UI for inviting participants
    }

    /**
     * UI for editing current participants that needs to be implemented when the button is pressed
     */
    public void editCurrentParticipants(){
        mainCtrl.switchToParticipantListScreen();
    }

    /**
     * UI for adding a participant that needs to be implemented when the button is pressed
     */
    public void addParticipants(){
        mainCtrl.switchToAddParticipant();
    }

    /**
     * UI for adding an expense that needs to be implemented when the button is pressed
     */
    public void addExpense(){
        mainCtrl.switchToAddExpense();
    }

    /**
     * set the name of the event in the event screen
     */
    private void updateEventText() {
        eventNameLabel.setText(event.getTitle());
        invitationCode.setText(event.getId());
    }

    /**
     * set the participants in the event screen that are part of the current event
     */
    private void updateParticipants(){
        StringBuilder participantsText = new StringBuilder();
        Iterator<Participant> participantIterator = event.getParticipants().iterator();
        while(participantIterator.hasNext()){
            Participant current = participantIterator.next();
            if(participantIterator.hasNext()) participantsText.
                    append(current.getName()).append(", ");
            else participantsText.append(current.getName());
        }
        if(participantsText.toString().isEmpty()) {
            participantsLabel.setText("There are no participants");
        }
        else participantsLabel.setText(participantsText.toString());
    }

    /**
     * show the participants of the current event
     */
    private void updateParticipantsDropdown(){
        cBoxParticipantExpenses.getItems().clear();
        //listViewExpensesParticipants.getItems().clear();
        for (Participant current : event.getParticipants()) {
            Label participantLabel = createParticipantLabel(current.getName(),
                current.getId());
            cBoxParticipantExpenses.getItems().add(current.getName());
        }
    }

    /**
     * In the context of this app, when choosing one of the participants
     * we want to see 3 buttons, 2 of which are related to the name of the participant.
     * This method generates those buttons and adds them to the hBox
     * @param name the name from the buttons
     */
    public void setButtonsNames(String name) {
        allExpenses = new Button("All");
        fromButton = new Button("From " + name);
        inButton = new Button("Including " + name);
        buttonsHBox.getChildren().clear();
        buttonsHBox.getChildren().add(allExpenses);
        buttonsHBox.getChildren().add(fromButton);
        buttonsHBox.getChildren().add(inButton);
    }

    /**
     * Creates a label for the participant and it
     * @param participantName the name of the participant we are creating a label for
     * @param participantId the id of the participant
     * @return the created label
     */
    public Label createParticipantLabel(String participantName, Long participantId) {
        Label participantLabel = new Label(participantName);
        participantLabel.setOnMouseEntered(mouseEvent -> {
            mainCtrl.getEventScene().setCursor(Cursor.HAND);
        });
        participantLabel.setOnMouseExited(mouseEvent -> {
            mainCtrl.getEventScene().setCursor(Cursor.DEFAULT);
        });
        participantLabel.setOnMouseClicked(mouseEvent -> {
            setButtonsNames(participantName);
        });
        return participantLabel;
    }

    /**
     * the action when we press the "All" button
     */
    public void showAllExpenses(){
        this.selectedExpenseListButton = allExpenses;
        refreshExpenseList();
    }

    private void refreshExpenseList() {
        Button selectedButton = selectedExpenseListButton;
        if(selectedButton==null) selectedButton = allExpenses;
        if(selectedButton==allExpenses) showAllExpenseList();
        else if(selectedButton==fromButton); //only From someone (unimplemented)
        else; //only Including someone (unimplemented)
    }

    /**
     * Generates a list of hBoxes stating with a human-readable String
     * that presents a quick overview of the expense presented
     * the method throws an error
     */
    public void showAllExpenseList (){
        expensesLogListView.getItems().clear();
        try {
            for (Expense expense : event.getExpenses()) {
                String log = expense.stringOnScreen();
                Label expenseText = generateExpenseLabel(expense.getId(), log);
                HBox expenseBox = new HBox(generateRemoveButton(expense.getId()), expenseText);
                expenseBox.setSpacing(10);
                hBoxMap.put(expense.getId(), expenseBox);
                expenseBox.setAlignment(CENTER_LEFT);
                expensesLogListView.getItems().add(expenseBox);
            }
        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a label that will later be added to the hBox from the expense listview
     * Furthermore, it allows the client to access set expense and edit it
     * @param expenseId the id of the expense we are creating a label for
     * @param expenseTitle the title of the expense
     * @return the created label
     */
    public Label generateExpenseLabel(long expenseId, String expenseTitle) {
        Label expense = new Label(expenseTitle);
        expense.setOnMouseEntered(mouseEvent -> {
            mainCtrl.getEventScene().setCursor(Cursor.HAND);
        });
        expense.setOnMouseExited(mouseEvent -> {
            mainCtrl.getEventScene().setCursor(Cursor.DEFAULT);
        });
        expense.setOnMouseClicked(mouseEvent -> {
            mainCtrl.switchToEditExpense(expenseId);
            });
        return expense;
    }

    /**
     * Generates a remove "button" for the expenses from the listview
     * Furthermore, it allows the client to remove set event by pressing the 'X'
     * @param expenseId the id of the expense to be generated
     * @return the generated button
     * @throws FileNotFoundException in case the image isn't found,
     * this exception is thrown
     */
    public ImageView generateRemoveButton (long expenseId) throws FileNotFoundException {
        FileInputStream input = null;
        input = new FileInputStream("client/src/main/resources/images/x_remove.png");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        int imgSize = 15;
        imageView.setFitHeight(imgSize);
        imageView.setFitWidth(imgSize);
        imageView.setPickOnBounds(true);
        imageView.setOnMouseEntered(mouseEvent -> {
            mainCtrl.getEventScene().setCursor(Cursor.HAND);
        });
        imageView.setOnMouseExited(mouseEvent -> {
            mainCtrl.getEventScene().setCursor(Cursor.DEFAULT);
        });
        imageView.setOnMouseClicked(mouseEvent -> {
            try {
                removeFromList(expenseId);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return imageView;
    }

    /**
     * Deletes an expense from the server. It also reflects it in the client
     * by deleting the set expense from the listview
     * @param expenseId the id of the expense we want to delete
     * @throws FileNotFoundException in case the file isn't found the exception is thrown
     */
    public void removeFromList(long expenseId) throws FileNotFoundException {
        server.deleteExpenseForEvent(event.getId(), expenseId);
        HBox hBox = hBoxMap.get(expenseId);
        hBoxMap.remove(expenseId);
        expensesLogListView.getItems().remove(hBox);
    }


    /**
     * UI for settling current debts
     * @param actionEvent on button click event
     */
    public void settleDebts(ActionEvent actionEvent) {
        mainCtrl.switchToSettleScreen();
    }

    /**
     * go back to the main screen
     * @param actionEvent when button is clicked
     */
    public void switchToMainScreen(ActionEvent actionEvent) {
        mainCtrl.showMainScreen();
    }

    /**
     * Sends a test email to the user
     */
    public void sendTestEmail() {
        emailHandler.sendTestEmail();
    }
}
