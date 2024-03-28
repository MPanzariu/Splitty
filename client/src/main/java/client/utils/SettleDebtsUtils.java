package client.utils;

import com.google.inject.Inject;
import commons.Expense;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.text.DecimalFormat;
import java.util.*;


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

    //Pseudocode adapted from: https://stackoverflow.com/questions/4554655/who-owes-who-money-optimization
    /***
     * Calculates at most N-1 transfer instructions for N participants
     * @param creditMap a Map of participants to credit(+)/debt(-)
     * @return a Set of Transfer instructions (sender, amount, receiver)
     */
    public Set<Transfer> calculateTransferInstructions(HashMap<Participant, Integer> creditMap){
        Set<Transfer> result = new HashSet<>();
        //We probably don't want to be mutating the inserted creditMap, re-running should yield the same result
        HashMap<Participant, Integer> processMap = new HashMap<>(creditMap);

        List<Map.Entry<Participant, Integer>> creditorsSorted = processMap.entrySet().stream()
                .filter(entry->entry.getValue()>0)
                .sorted(Map.Entry.comparingByValue())
                .toList();

        List<Map.Entry<Participant, Integer>> debtorsSorted = processMap.entrySet().stream()
                .filter(entry->entry.getValue()<0)
                .sorted(Map.Entry.comparingByValue())
                .toList();

        ArrayList<Map.Entry<Participant, Integer>> creditors = new ArrayList<>(creditorsSorted);
        ArrayList<Map.Entry<Participant, Integer>> debtors = new ArrayList<>(debtorsSorted);

        for(Map.Entry<Participant, Integer> debtorEntry: debtors){
            Participant debtor = debtorEntry.getKey();
            int negativeBalance = debtorEntry.getValue();
            while(negativeBalance < 0){
                Map.Entry<Participant, Integer> creditorEntry = null;
                try {
                    creditorEntry = creditors.getLast();
                } catch (NoSuchElementException e){
                    throwBadBalanceException(creditMap, processMap);
                }
                int credit = creditorEntry.getValue();
                int amountToTransfer = Integer.min(-negativeBalance, credit);

                creditorEntry.setValue(credit - amountToTransfer);
                negativeBalance += amountToTransfer;
                if(creditorEntry.getValue()==0){
                    creditors.removeLast();
                }

                Transfer transfer = new Transfer(debtor, amountToTransfer, creditorEntry.getKey());
                result.add(transfer);
            }
            debtorEntry.setValue(negativeBalance);
        }

        //Sanity checks to ensure amounts in/out were balanced
        creditors.forEach(entry->{
            if(entry.getValue()!=0) throwBadBalanceException(creditMap, processMap);
        });
        debtors.forEach(entry->{
            if(entry.getValue()!=0) throwBadBalanceException(creditMap, processMap);
        });

        return result;
    }

    /***
     * Generates an Exception to be thrown when the credits and debits do not match
     * @param creditMap the CreditMap to print for debug purposes
     * @param processMap the CreditMap with mid-processing changes applied to it
     */
    private void throwBadBalanceException(HashMap<Participant, Integer> creditMap,
                                          HashMap<Participant, Integer> processMap){
        throw new IllegalArgumentException("Debts do not balance!\n"
                + "Provided input:\n" + creditMap
                + "Current processing state:\n" + processMap);
    }

    /***
     * Generates the onClick action for a button that settles a particular debt
     * @param participantOwes the participant sending money
     * @param amount the amount in cents
     * @param participantOwedTo the participant receiving money
     * @param eventId the corresponding event
     * @return the action a button should perform to settle the debt
     */
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

    /***
     * Generates a string representation of a debt
     * @param participantOwes the participant owing money
     * @param amount the amount owed
     * @param participantOwedTo the participant receiving money
     * @return a String stating who owes who and how much
     */
    public String createDebtString(String participantOwes,
                                   int amount,
                                   String participantOwedTo) {
        int amountPositive = Math.abs(amount);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedPrice = decimalFormat.format(amountPositive / 100.0) + "\u20ac";

        StringBuilder result = new StringBuilder(participantOwes);
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

    /***
     * Generates a String representing all bank details for a Participant
     * @param participant the Participant to check
     * @return a String representation of a Participant's bank details
     */
    public String getBankDetails(Participant participant) {
        String name = participant.getLegalName();
        String iban = participant.getIban();
        String bic = participant.getBic();

        if(Objects.equals(name, "")) name = "(MISSING)";
        if(Objects.equals(iban, "")) iban = "(MISSING)";
        if(Objects.equals(bic, "")) bic = "(MISSING)";

        StringBuilder result = new StringBuilder();
        if(participant.hasBankAccount()){
            result.append("Bank information available, transfer the money to:\n");
        } else {
            result.append("Full bank information unavailable:\n");
        }

        result.append("Account Holder: ");
        result.append(name);
        result.append("\n");
        result.append("IBAN: ");
        result.append(iban);
        result.append("\n");
        result.append("BIC: ");
        result.append(bic);

        return result.toString();
    }
}
