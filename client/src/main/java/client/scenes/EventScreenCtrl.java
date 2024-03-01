package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class EventScreenCtrl implements Initializable{
    @FXML
    private Button sendInvites;
    @FXML
    private Button addExpense;
    @FXML
    private Button allExpensesButton;
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
    private ComboBox<String> cBoxParticipantExpenses;
    @FXML
    private ListView<String> listViewExpensesParticipants;
    @FXML
    private ListView<String> expensesLogListView;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Translation translation;
    private Event event;

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
    }

    /**
     * Initialize basic features of the application, bind text to be translated,
     * set images for edit and add participants buttons
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
        sendInvites.textProperty().bind(translation.getStringBinding("Event.Button.SendInvites"));
        participantsName.textProperty().bind(translation.getStringBinding("Participants.DisplayName.EventScreen"));
        expenseLabel.textProperty().bind(translation.getStringBinding("Expense.Label.Display.EventScreen"));
        addExpense.textProperty().bind(translation.getStringBinding("Event.Button.AddExpense"));
        allExpensesButton.textProperty().bind(translation.getStringBinding("Event.Button.ShowAllExpenses"));
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
    }

    /**
     * UI for inviting participants that needs to be implemented when the button is pressed
     */
    public void inviteParticipants(){
        //TO DO, implement UI for inviting participants
        System.out.println(event.toString());
    }

    /**
     * UI for editing current participants that needs to be implemented when the button is pressed
     */
    public void editCurrentParticipants(){
        //TO DO, implement UI for editing exisiting participants
        System.out.println(event.toString());
    }

    /**
     * UI for adding a participant that needs to be implemented when the button is pressed
     */
    public void addParticipants(){
        //TO DO, implement UI for adding participants
        System.out.println(event.toString());
    }

    /**
     * UI for adding an expense that needs to be implemented when the button is pressed
     */
    public void addExpense(){
        //TO DO, implement UI for adding expenses
        System.out.println(event.toString());
    }

    /**
     * set the name of the event in the event screen
     * @param event the current event we are at
     */
    public void setEvent(Event event) {
        this.event = event;
        eventNameLabel.setText(event.getTitle());
    }

    /**
     * set the participants in the evenet screen that are part of the current event
     * @param event the current event we are at
     */
    public void setParticipants(Event event){
        this.event = event;
        String participantsText = "";
        Set<Participant> participants = event.getParticipants();
        Iterator<Participant> participantIterator = participants.iterator();
        while(participantIterator.hasNext()){
            Participant current = participantIterator.next();
            if(participantIterator.hasNext())participantsText += current.getName() + ", ";
            else participantsText += current.getName();
        }
        if(participantsText.equals(""))participantsLabel.setText("There are no participants");
        else participantsLabel.setText(participantsText);
    }

    /**
     * show the participants of the current event
     * @param event the event we are currently looking at
     */
    public void setParticipantsForExpenses(Event event){
        this.event = event;
        Set<Participant> participants = event.getParticipants();
        Iterator<Participant> participantIterator = participants.iterator();
        while(participantIterator.hasNext()) {
            Participant current = participantIterator.next();
            cBoxParticipantExpenses.getItems().add(current.getName());
            listViewExpensesParticipants.getItems().add(current.getName());
        }
    }

    /**
     * the action when we press the "All" button
     * @param actionEvent on button click event
     */
    public void showAllExpensesSettled(ActionEvent actionEvent) {
        expensesLogListView.getItems().clear();
        List<Expense> settledExpenses = event.getSettledExpenses();
        for(int i = 0; i < settledExpenses.size(); i++){
            String log = "";
            log+=settledExpenses.get(i).getOwedTo().getName();
            log+= " paid ";
            log+=settledExpenses.get(i).getPriceInCents();
            log+= " for ";
            log+=settledExpenses.get(i).getName();
            expensesLogListView.getItems().add(log);
        }
    }
}
