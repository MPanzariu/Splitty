package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import commons.Expense;
import commons.Event;
import commons.Participant;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.awt.*;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;

public class ExpenseScreenCtrl implements Initializable{
    private final ServerUtils server;
    @FXML
    private Label addEditExpense;
    @FXML
    private Label paidBy;
    @FXML
    private ComboBox<String> choosePayer;
    @FXML
    private Label purpose;
    @FXML
    private TextField expensePurpose;
    @FXML
    private Label amount;
    @FXML
    private TextField sum;
    @FXML
    private ComboBox<String> currency;
    @FXML
    private Label date;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label splitMethod;
    @FXML
    private Label expenseType;
    @FXML
    private CheckBox splitBetweenAllCheckBox;
    @FXML
    private Label splitBetweenAllLabel;
    @FXML
    private CheckBox splitBetweenCustomCheckBox;
    @FXML
    private Label getSplitBetweenCustomLabel;
    @FXML
    private Button cancel;
    @FXML
    private Button confirm;
    @FXML
    private Label errorParticipants;
    @FXML
    private Label errorNoPurpose;
    @FXML
    Label errorAmount;
    @FXML
    Label errorDate;
    @FXML
    Label errorSplitMethod;
    private final MainCtrl mainCtrl;
    private Event currentEvent;
    private final Translation translation;


    @Inject
    public ExpenseScreenCtrl (ServerUtils server, MainCtrl mainCtrl,
                              Translation translation) {
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        this.server = server;
        //currency.setItems(FXCollections.observableArrayList("EUR"));
    }

    /**
     * responsible for setting up the fields that need to be translated
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currency.setItems(FXCollections.observableArrayList("EUR"));
        choosePayer.setItems(getParticipantList());
        binds();
        splitBetweenAllCheckBox.setOnAction(event -> {
            if (splitBetweenAllCheckBox.isSelected()) {
                splitBetweenCustomCheckBox.setSelected(false);
            }
        });

        splitBetweenCustomCheckBox.setOnAction(event -> {
            if (splitBetweenCustomCheckBox.isSelected()) {
                splitBetweenAllCheckBox.setSelected(false);
            }
        });
    }

    public ObservableList<String> getParticipantList() {
        Set<Participant> participants;
        if(currentEvent == null|| currentEvent.getParticipants() == null)
            participants = new HashSet<>();
        else participants = currentEvent.getParticipants();
        Iterator<Participant> iterator = participants.iterator();
        List<String> names = new ArrayList<>();
        while(iterator.hasNext()) {
            Participant participant = iterator.next();
            names.add(participant.getName());
        }
        return FXCollections.observableArrayList(names);
    }

    private void binds() {
        addEditExpense.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.Add"));
        paidBy.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.paid"));
        purpose.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.purpose"));
        expensePurpose.promptTextProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.purpose"));
        amount.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.amount"));
        sum.promptTextProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.amount"));
        date.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.date"));
        datePicker.promptTextProperty()
                .bind(translation.getStringBinding("Expense.DatePicker.Display.date"));
        splitMethod.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.split"));
        splitBetweenAllLabel.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.splitAll"));
        getSplitBetweenCustomLabel.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.splitCustom"));
        cancel.textProperty()
            .bind(translation.getStringBinding("Expense.Button.Cancel"));
        confirm.textProperty()
            .bind(translation.getStringBinding("Expense.Button.Confirm"));
        expenseType.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.expenseType"));
        errorParticipants.textProperty()
            .bind(translation.getStringBinding("empty"));
        errorNoPurpose.textProperty()
            .bind(translation.getStringBinding("empty"));
        errorAmount.textProperty()
            .bind(translation.getStringBinding("empty"));
        errorDate.textProperty()
            .bind(translation.getStringBinding("empty"));
        errorSplitMethod.textProperty()
            .bind(translation.getStringBinding("empty"));
    }

    /**
     * Assign the event corresponding to the current expense
     * @param event the event
     */
    public void refresh(Event event) {
        this.currentEvent = event;
        currency.setItems(FXCollections.observableArrayList("", "EUR"));
        choosePayer.setItems(getParticipantList());
    }

    /**
     * When pressing the Cancel button it takes the user
     * back to the Event Screen
     * @param actionEvent the action of clicking the button
     */
    public void switchToEventScreen(ActionEvent actionEvent) {
        mainCtrl.switchToEventScreen();
    }

    /**
     * resets all the fields in the expenseScreen
     */
    public void resetAll() {
        resetAmount();
        resetPurpose();
        resetDate();
        resetCurrency();
    }
    /**
     * resets the amount inserted in the amount TextField
     */
    public void resetAmount() {
        this.sum.clear();
    }

    /**
     * resets the text inserted in the purpose TextField
     */
    public void resetPurpose() {
        this.expensePurpose.clear();
    }

    /**
     * resets the date chosen for the datePicker field
     */
    public void resetDate() {
        this.datePicker.getEditor().clear();
    }

    /**
     * (Theoretically) resets the currency inserted in the currency
     * ComboBox
     */
    public void resetCurrency() {
        this.currency.getEditor().clear();
    }

    /**
     * Creates a new expense based on the information provided
     * in the ExpenseScreen
     */
    public Expense createNewExpense() {
        String name = expensePurpose.getText();
        String priceInMoney = sum.getText();
        double price = 0;
        try {
            price = Double.parseDouble(priceInMoney);
        }
        catch (IllegalArgumentException e) {
            System.out.println("Please enter a valid number");
        }
        int priceInCents = (int) Math.ceil(price * 100);
        //change in case of wanting to implement another date system
        LocalDate date = datePicker.getValue();
        Date expenseDate = null;
        if(date != null)
            expenseDate = Date.valueOf(datePicker.getValue());

        // this has to be a participant that actually exists in the database
        // you can not in fact just create a new one
        //
        /*Participant participant =
            new Participant(choosePayer.getValue(), currentEvent);*/
        // I'd suggest doing something with currentEvent.getParticipants()
        Iterator<Participant> participantIterator = currentEvent.getParticipants().iterator();
        Participant participant = participantIterator.hasNext()
                ? participantIterator.next() : null;

            return new Expense(name, priceInCents, expenseDate, participant);
    }

    /**
     * Adds the specified expense to the server
     * @param expense the provided expense
     */
    public void addExpenseToTheServer(Expense expense) {
        server.addExpense(currentEvent.getId(), expense);
    }
    /**
     * Needs revision
     */
    public void addExpenseToEvenScreen(ActionEvent actionEvent) {
        boolean toAdd = true;
        Expense expense = createNewExpense();
        if(expense.getOwedTo() == null) {
            errorParticipants.textProperty()
                .bind(translation.getStringBinding("Expense.Label.NoParticipants"));
            toAdd = false;
        }
        if(expense.getName() == null || expense.getName().isEmpty()) {
            errorNoPurpose.textProperty()
                .bind(translation.getStringBinding("Expense.Label.NoPurpose"));
            toAdd = false;
        }
        if(expense.getPriceInCents() == 0) {
            errorAmount.textProperty()
                .bind(translation.getStringBinding("Expense.Label.InvalidAmount"));
            toAdd = false;
        }
        if(expense.getDate() == null) {
            errorDate.textProperty()
                .bind(translation.getStringBinding("Expense.Label.InvalidDate"));
            toAdd = false;
        }
        if(!splitBetweenCustomCheckBox.isSelected()
            && !splitBetweenAllCheckBox.isSelected()){
            errorSplitMethod.textProperty()
                .bind(translation.getStringBinding("Expense.Label.InvalidSplitMethod"));
            toAdd = false;
        }
        if(toAdd) {
            addExpenseToTheServer(expense);
            mainCtrl.switchToEventScreen();
        }
    }

    //TODO: 1.Fixing the bindings
    //TODO: 2.Getting the participant that paid (after the participant UI is implemented)
    //TODO: 3.Adding the expense to the EventScreen
    //TODO: 4.Making the user able to choose between participants that can pay(for the custom participants button)
}
