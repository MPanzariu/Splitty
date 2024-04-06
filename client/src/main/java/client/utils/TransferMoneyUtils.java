package client.utils;

import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class TransferMoneyUtils {
    private ObjectProperty<Participant> from;
    private BooleanProperty isFromErrorVisible;
    private ObjectProperty<EventHandler<ActionEvent>> fromAction;
    private ObjectProperty<Participant> to;
    private BooleanProperty isToErrorVisible;
    private ObjectProperty<EventHandler<ActionEvent>> toAction;
    private StringProperty amount;
    private BooleanProperty isAmountErrorVisible;
    private ObjectProperty<String> selectedCurrency;
    private final ObservableList<Participant> participants = FXCollections.observableArrayList();
    private final ObservableList<String> currencies = FXCollections.observableArrayList();
    private final ChangeListener<String> amountChangeListener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            isAmountErrorVisible.set(isAmountInvalid());
            amount.removeListener(this);
        }
    };
    private StringProperty expenseMessage;
    private final ServerUtils server;
    private Event event;

    @Inject
    public TransferMoneyUtils(ServerUtils server) {
        this.server = server;
    }

    public void initializeFrom() {
        fromAction.setValue(event -> {
            isFromErrorVisible.set(false);
            fromAction.setValue(null);
        });
    }

    public void initializeTo() {
        toAction.setValue(event -> {
            isToErrorVisible.set(false);
            toAction.setValue(null);
        });
    }

    public boolean hasErrors() {
        if(from.getValue() == null || to.getValue() == null) {
            isFromErrorVisible.set(from.getValue() == null);
            isToErrorVisible.set(to.getValue() == null);
        }
        if(isAmountInvalid()) {
            isAmountErrorVisible.set(true);
            amount.addListener(amountChangeListener);
        }
        return isFromErrorVisible.get() || isToErrorVisible.get() || isAmountErrorVisible.get();
    }

    public boolean hasInvalidParticipants() {
        return from.get().equals(to.get());
    }

    private boolean isAmountInvalid() {
        try {
            BigDecimal b = new BigDecimal(amount.getValue());
            b = b.setScale(2, RoundingMode.HALF_UP);
            return b.longValue() <= 0;
        } catch(NumberFormatException e) {
            return true;
        }
    }

    /**
     * Creates an expense for the money transfer which is sent to the backend.
     * If A transfer an X amount of money to B, then the expense will be modelled as A owes -X to B.
     * This allows the client to recognize this expense as a money transfer.
     */
    public void transferMoney() {
        Expense transfer = new Expense("Money Transfer", Integer.parseInt(amount.get()) * -100,
                new Date(), to.get());
        transfer.addParticipantToExpense(from.get());
        server.addExpense(event.getId(), transfer);
    }

    public void refresh(Event event) {
        this.event = event;
        participants.setAll(event.getParticipants());
        // This should change when there is a variable for all currencies supported by our application.
        currencies.setAll("EUR");

        initializeFrom();
        initializeTo();

        from.set(null);
        to.set(null);
        amount.set("");
        selectedCurrency.set(currencies.getFirst());

        isFromErrorVisible.set(false);
        isToErrorVisible.set(false);
        isAmountErrorVisible.set(false);

        amount.removeListener(amountChangeListener);
    }

    public void setIsAmountErrorVisible(BooleanProperty property) {
        isAmountErrorVisible = property;
    }

    public void setIsFromErrorVisible(BooleanProperty isVisible) {
        isFromErrorVisible = isVisible;
    }

    public void setFrom(ObjectProperty<Participant> participant) {
        from = participant;
    }

    public void setFromAction(ObjectProperty<EventHandler<ActionEvent>> action) {
        fromAction = action;
    }

    public void setTo(ObjectProperty<Participant> participant) {
        to = participant;
    }

    public void setToAction(ObjectProperty<EventHandler<ActionEvent>> action) {
        toAction = action;
    }

    public void setIsToErrorVisible(BooleanProperty isVisible) {
        isToErrorVisible = isVisible;
    }

    public void setAmount(StringProperty amount) {
        this.amount = amount;
    }

    public ObservableList<Participant> getParticipants() {
        return participants;
    }

    public ObservableList<String> getCurrencies() {
        return currencies;
    }

    public void setSelectedCurrency(ObjectProperty<String> currency) {
        selectedCurrency = currency;
    }
}
