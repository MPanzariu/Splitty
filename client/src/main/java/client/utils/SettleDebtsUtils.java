package client.utils;

import com.google.inject.Inject;
import commons.Expense;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.text.NumberFormat;
import java.util.Locale;


public class SettleDebtsUtils {

    private final Translation translation;
    private final ServerUtils server;


    /***
     * Constructor for the utility class for the SettleDebts screen
     * @param translation - the translation to use
     * @param server - the severUtils to use
     */
    @Inject
    public SettleDebtsUtils(Translation translation, ServerUtils server) {
        this.translation = translation;
        this.server = server;
    }

    /***
     * Generates an expense that settled a debt (can not be implemented yet)
     * @param participantOwes the participant sending
     * @param owed the amount owed
     * @param participantOwedTo the participant receiving
     * @return an Expense that settles the debt fully
     */
    public Expense createSettlementExpense(Participant participantOwes,
                                           int owed,
                                           Participant participantOwedTo) {

        //This method can only be made when splitting an expense between only some people works
        //This method SHOULD be implemented properly in the Open Debts optional requirement
        return new Expense();
    }


    public EventHandler<ActionEvent> createSettleAction(Participant participantOwes,
                                  int amount,
                                  Participant participantOwedTo, String eventId){
        EventHandler<ActionEvent> onClick = (actionEvent) -> {
            Expense settleExpense = createSettlementExpense(participantOwes,
                    amount,
                    participantOwedTo);
            //server.addExpense(eventId, settleExpense);
            //This should not do anything yet! (this is extra functionality)
            //This code should be uncommented when the Open Debts requirement is worked on
        };
        return onClick;
    }

    public String createDebtString(Participant participantOwes,
                                   int amount,
                                   Participant participantOwedTo) {
        Locale locale = translation.getLocale();
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        int amountPositive = Math.abs(amount);
        String formattedPrice = format.format(amountPositive / 100.0);

        String name = participantOwes.getName();
        StringBuilder result = new StringBuilder(name);
        if(amount==0) result.append(" owes nothing!");
        else if(amount<0){
            result.append(" owes ");
            result.append(formattedPrice);
            result.append(" to the group");
        } else {
            result.append( " is owed ");
            result.append(formattedPrice);
            result.append(" by the group");
        }
        return result.toString();
    }
}
