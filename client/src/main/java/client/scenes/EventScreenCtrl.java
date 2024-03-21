package client.scenes;

import client.utils.ServerUtils;
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

public class EventScreenCtrl implements Initializable{
    @FXML
    private TextField invitationCode;
    @FXML
    private Button addExpense;
    @FXML
    private Button allExpensesButton;
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
    private ListView<String> listViewExpensesParticipants;
    @FXML
    private ListView<HBox> expensesLogListView;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    private Event event;
    private Map<Long, HBox> hBoxMap;
    /**
     * Constructor
     * @param server the ServerUtils instance
     * @param mainCtrl the MainCtrl instance
     * @param translation the Translation to use
     */
    @Inject
    public EventScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        this.event = null;
        this.hBoxMap = new HashMap<>();
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
        allExpensesButton.textProperty().bind(translation.getStringBinding("Event.Button.ShowAllExpenses"));
        settleDebtsButton.textProperty().bind(translation.getStringBinding("Event.Button.SettleDebts"));
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
        mainCtrl.switchToAddParticipantExistent();
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
        listViewExpensesParticipants.getItems().clear();
        for (Participant current : event.getParticipants()) {
            cBoxParticipantExpenses.getItems().add(current.getName());
            listViewExpensesParticipants.getItems().add(current.getName());
        }
    }

    /**
     * the action when we press the "All" button
     * @param actionEvent on button click event
     */
    public void showExpenses() throws FileNotFoundException {
        showExpenseList();
    }

    public void showExpenseList () throws FileNotFoundException {
        expensesLogListView.getItems().clear();
        for(Expense expense: event.getExpenses()) {
            String log = expense.stringOnScreen();
            Label expenseText = generateExpenseLabel(expense.getId(), log);
            HBox expenseBox = new HBox(generateRemoveButton(expense.getId()), expenseText);
            expenseBox.setSpacing(10);
            hBoxMap.put(expense.getId(), expenseBox);
            expenseBox.setAlignment(CENTER_LEFT);
            expensesLogListView.getItems().add(expenseBox);
        }
    }

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

    public void removeFromList(long id) throws FileNotFoundException {
        server.deleteExpenseForEvent(event.getId(), id);
        HBox hBox = hBoxMap.get(id);
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
}
