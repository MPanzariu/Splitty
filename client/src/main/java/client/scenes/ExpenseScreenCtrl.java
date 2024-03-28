package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import commons.Expense;
import commons.Event;
import commons.Participant;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class ExpenseScreenCtrl implements Initializable, SimpleRefreshable {
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
    private CheckBox splitBetweenCustomCheckBox;
    @FXML
    private Button cancel;
    @FXML
    private Button confirm;
    @FXML
    private Label errorParticipants;
    @FXML
    private Label errorNoPurpose;
    @FXML
    private Label errorAmount;
    @FXML
    private Label errorDate;
    @FXML
    private Label errorSplitMethod;
    @FXML
    private VBox participantsVBox;
    private final MainCtrl mainCtrl;
    private Event currentEvent;
    private final Translation translation;
    private long expenseId;
    private List<CheckBox> participantCheckBoxes;

    /**
     *
     * @param server the server to which the client is connected
     * @param mainCtrl the main controller
     * @param translation the class that manages translations
     */
    @Inject
    public ExpenseScreenCtrl (ServerUtils server, MainCtrl mainCtrl,
                              Translation translation) {
        this.mainCtrl = mainCtrl;
        this.translation = translation;
        this.server = server;
    }

    /**
     * responsible for setting up the fields that need to be translated
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currency.setItems(FXCollections.observableArrayList("EUR"));
        participantCheckBoxes = new ArrayList<>();
        choosePayer.setItems(getParticipantList());
        binds();
        splitBetweenAllCheckBox.setOnAction(event -> {
            if (splitBetweenAllCheckBox.isSelected()) {
                splitBetweenCustomCheckBox.setSelected(false);
                participantsVBox.getChildren().clear();
            }
        });

        splitBetweenCustomCheckBox.setOnAction(event -> {
            if (splitBetweenCustomCheckBox.isSelected()) {
                splitBetweenAllCheckBox.setSelected(false);
                addParticipants();
            }
            if(!splitBetweenCustomCheckBox.isSelected()) {
                participantsVBox.getChildren().clear();
            }
        });
    }


    /**
     * Method used for getting the participants that will be added
     * in the combobox for the Paid by field
     * @return the participant list
     */
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

    /**
     * Binds each text to a key in order to be used for translation
     */
    private void binds() {
        addEditExpense.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.Add"));
        paidBy.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.paid"));
        choosePayer.promptTextProperty()
                .bind(translation.getStringBinding("Expense.ComboBox.payer"));
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
        splitBetweenAllCheckBox.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.splitAll"));
        splitBetweenCustomCheckBox.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.splitCustom"));
        cancel.textProperty()
            .bind(translation.getStringBinding("Expense.Button.Cancel"));
        confirm.textProperty()
            .bind(translation.getStringBinding("Expense.Button.Confirm"));
        expenseType.textProperty()
            .bind(translation.getStringBinding("Expense.Label.Display.expenseType"));
        bindToEmpty();
    }

    /**
     * Binds the error fields to empty
     */
    public void bindToEmpty() {
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
        bindToEmpty();
    }

    /***
     * Specifies if the screen should be live-refreshed
     * @return true if changes should immediately refresh the screen, false otherwise
     */
    @Override
    public boolean shouldLiveRefresh() {
        return false;
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
     * @return the newly created expense
     */
    public Expense createNewExpense() {
        String name = getTextFieldText(expensePurpose);
        String priceInMoney = getTextFieldText(sum);
        double price = 0;
        try {
            price = Double.parseDouble(priceInMoney);
        }
        catch (IllegalArgumentException e) {
            System.out.println("Please enter a valid number");
        }
        int priceInCents = (int) Math.ceil(price * 100);
        //change in case of wanting to implement another date system
        LocalDate date = getLocalDate(datePicker);
        Date expenseDate = null;
        if(date != null)
            expenseDate = Date.valueOf(datePicker.getValue());

        String participantName = getComboBox(choosePayer);
        Iterator<Participant> participantIterator = currentEvent.getParticipants().iterator();
        Participant participant = null;
        while(participantIterator.hasNext()){
            participant = participantIterator.next();
            if(participant.getName().equals(participantName)) break;
        }
        Expense resultExpense = new Expense(name, priceInCents, expenseDate, participant);
        Set<Participant> participantSet = getParticipantsForExpense();
        for(Participant part: participantSet) {
            resultExpense.addParticipantToExpense(part);
        }
        return resultExpense;
    }

    /**
     *
     * @param textField a text field from the screen
     * @return the text inside the text field
     */
    public String getTextFieldText(TextField textField) {
        return textField.getText();
    }

    /**
     *
     * @param datePicker a chosen date picker
     * @return the date from the date picker
     */
    public LocalDate getLocalDate(DatePicker datePicker) {
        return datePicker.getValue();
    }

    /**
     *
     * @param comboBox the specified comboBox
     * @return the text from the comboBox
     */
    public String getComboBox(ComboBox<String> comboBox) {
        return comboBox.getValue();
    }
    /**
     * Adds the specified expense to the server
     * @param expense the provided expense
     */
    public void addExpenseToTheServer(Expense expense) {
        server.addExpense(currentEvent.getId(), expense);
    }

    /**
     * when editing an expense this method makes sure that
     * the fields are filled according to the expense that is being edited
     * @param id the id of the expense
     */
    public void setExpense(long id) {
        Set<Expense> expenses = currentEvent.getExpenses();
        Expense expense = null;
        for(Expense exp: expenses){
            if(exp.getId() == id) {
                expense = exp;
                break;
            }
        }
        if(expense == null)
            return;
        expensePurpose.setText(expense.getName());
        sum.setText(String.valueOf((double) expense.getPriceInCents()/100));
        choosePayer.getEditor().setText(expense.getOwedTo().getName());
        datePicker.getEditor().setText(String.valueOf(expense.getDate())); //needs revision
        expenseId = id;
    }

    /**
     * Edits the expense with the provided id
     * @param expenseId the id of the expense that is edited
     * @param expense the expense we want to replace the current
     * expense with
     */
    public void editExpenseOnServer(long expenseId, Expense expense) {
        server.editExpense(currentEvent.getId(), expenseId, expense);
    }
    /**
     *
     * @param actionEvent the action of clicking the confirm button
     */
    public void addExpenseToEvenScreen(ActionEvent actionEvent) {
        boolean toAdd = true;
        Expense expense = createNewExpense();
        bindToEmpty();
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
        if(expense.getPriceInCents() <= 0) {
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
            if(expenseId == 0)
                addExpenseToTheServer(expense);
            else {
                editExpenseOnServer(expenseId, expense);
                expenseId = 0;
            }
            mainCtrl.switchToEventScreen();
        }
    }

    /**
     * Adds the selected participants to the current expense
     * @return a set of all participants for the expense
     */
    public Set<Participant> getParticipantsForExpense() {
        if(splitBetweenAllCheckBox.isSelected())
            return currentEvent.getParticipants();
        Set<Participant> result = new HashSet<>();
        Set<Participant> participants = currentEvent.getParticipants();
        for(CheckBox checkBox: participantCheckBoxes) {
            boolean found = false;
            if(checkBox.isSelected()) {
                String name = checkBox.getText();
                for(Participant participant: participants) {
                    if(participant.getName().equals(name)) {
                        result.add(participant);
                        found = true;
                        break;
                    }
                }
                if(!found)
                    throw new EntityNotFoundException("The participant doesn't exist anymore");
            }
        }
        return result;
    }

    /**
     * Generates a list of checkboxes with the names of
     * the event participants
     */
    public void addParticipants() {
        Set<Participant> participants = currentEvent.getParticipants();
        for(Participant participant: participants) {
            CheckBox participantToPay = new CheckBox(participant.getName());
            participantsVBox.getChildren().add(participantToPay);
            participantCheckBoxes.add(participantToPay);
        }
    }
}
