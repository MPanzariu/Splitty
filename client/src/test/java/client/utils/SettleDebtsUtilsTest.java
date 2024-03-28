package client.utils;

import commons.Participant;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SettleDebtsUtilsTest {
    @InjectMocks
    SettleDebtsUtils sut;

    @Mock
    ServerUtils server;
    @Mock
    Translation translation;

    Participant participant1;
    Participant participant2;
    Participant participant3;

    @BeforeEach
    void setup(){
        participant1 = new Participant("Vox");
        participant2 = new Participant("Val");
        participant3 = new Participant("Vel");
    }

    /***
     * Randomized transfers result in correct outcome
     */
    @Test
    void randomTransferInstructions(){
        HashMap<Participant, Integer> creditMap = new HashMap<>();
        Random rng = new Random();

        Participant participant4 = new Participant("VVV");

        int rand1 = generateRandom(rng);
        int rand2 = generateRandom(rng);
        int rand3 = generateRandom(rng);
        int balancer = -(rand1+rand2+rand3);

        creditMap.put(participant1, rand1);
        creditMap.put(participant2, rand2);
        creditMap.put(participant3, rand3);
        creditMap.put(participant4, balancer);

        var result = sut.calculateTransferInstructions(creditMap);
        assertTrue(result.size()<creditMap.size());

        //Simulate transfers
        for (Transfer transfer:
             result) {
            int balSender = creditMap.get(transfer.sender());
            int balReceiver = creditMap.get(transfer.receiver());
            creditMap.put(transfer.sender(), balSender + transfer.amount());
            creditMap.put(transfer.receiver(), balReceiver - transfer.amount());
        }

        creditMap.forEach((key, value) -> assertEquals(0, (int) value));
    }

    /***
     * Generates a random number between min and max
     */
    private int generateRandom(Random random){
        int min = -1000;
        int max = 1000;
        return random.nextInt(max - min) + min;
    }

    /***
     * One debtor can pay off multiple people
     */
    @Test
    void splitDebtTransferInstructions(){
        HashMap<Participant, Integer> creditMap = new HashMap<>();

        creditMap.put(participant1, -25);
        creditMap.put(participant2, 10);
        creditMap.put(participant3, 15);

        Transfer transfer1 = new Transfer(participant1, 10, participant2);
        Transfer transfer2 = new Transfer(participant1, 15, participant3);

        var result = sut.calculateTransferInstructions(creditMap);

        assertTrue(result.contains(transfer1));
        assertTrue(result.contains(transfer2));
    }

    /***
     * One creditor can receive from multiple people
     */
    @Test
    void splitCreditTransferInstructions(){
        HashMap<Participant, Integer> creditMap = new HashMap<>();

        creditMap.put(participant1, 100);
        creditMap.put(participant2, -25);
        creditMap.put(participant3, -75);

        Transfer transfer1 = new Transfer(participant2, 25, participant1);
        Transfer transfer2 = new Transfer(participant3, 75, participant1);

        var result = sut.calculateTransferInstructions(creditMap);

        assertTrue(result.contains(transfer1));
        assertTrue(result.contains(transfer2));
    }

    /***
     * Net positive amounts are rejected
     */
    @Test
    void netPositiveTransferInstructions(){
        HashMap<Participant, Integer> creditMap = new HashMap<>();

        creditMap.put(participant1, 100);
        creditMap.put(participant2, -10);
        creditMap.put(participant3, -20);

        assertThrows(IllegalArgumentException.class, () -> sut.calculateTransferInstructions(creditMap));
    }

    /***
     * Net negative amounts are rejected
     */
    @Test
    void netNegativeTransferInstructions(){
        HashMap<Participant, Integer> creditMap = new HashMap<>();

        creditMap.put(participant1, 100);
        creditMap.put(participant2, -70);
        creditMap.put(participant3, -50);

        assertThrows(IllegalArgumentException.class, () -> sut.calculateTransferInstructions(creditMap));
    }

    /***
     * [FEATURE NOT YET IMPLEMENTED]
     * Checks if the Expense generated to settle a normal debt is correct
     */
    @Test
    void createSettlementExpense() {
        var result = sut.createSettlementExpense(participant1, 7, participant2);
        assertNotNull(result);
    }

    /***
     * [FEATURE NOT YET IMPLEMENTED]
     * Checks if the Action executed when the Mark Received button is pressed
     * settles a normal debt correctly
     */
    @Test
    void createSettleAction() {
        EventHandler<ActionEvent> result = sut.createSettleAction(participant1, 7, participant2, "ABC123");
        result.handle(new ActionEvent());
        assertNotNull(result);
    }

    /***
     * [TO BE CHANGED/REMOVED WHEN MORE FEATURES ARE IMPLEMENTED]
     * Checks if text generated for someone with positive balance
     * fits the structure and has the correct details
     */
    @Test
    void stringPositive() {
        var result = sut.createDebtString("A", 7, "B");
        assertTrue(result.contains("A"));
        assertTrue(result.contains("is owed"));
        assertTrue(result.contains("0.07"));
    }

    /***
     * [TO BE CHANGED WHEN MORE FEATURES ARE IMPLEMENTED]
     * Checks if text generated for someone owing money
     * fits the structure and has the correct details
     */
    @Test
    void stringNegative() {
        var result = sut.createDebtString("A", -7, "B");
        assertTrue(result.contains("A"));
        assertTrue(result.contains("owes"));
        assertTrue(result.contains("0.07"));
    }

    /***
     * [TO BE CHANGED/REMOVED WHEN MORE FEATURES ARE IMPLEMENTED]
     * Checks if text generated for someone with no balance at all
     * fits the structure and has the correct details
     */
    @Test
    void stringZero() {
        var result = sut.createDebtString("A", 0, "B");
        assertTrue(result.contains("A"));
        assertTrue(result.contains("owes"));
        assertFalse(result.contains("to"));
    }

    /***
     * Checks if a Participant with all bank details
     * has their payment information generated properly
     */
    @Test
    void bankFull(){
        String name = "Vox V.";
        String iban = "NL02ABNA0123456789";
        String bic = "ABNANL2AXXX";

        participant1.setLegalName(name);
        participant1.setIban(iban);
        participant1.setBic(bic);

        var result = sut.getBankDetails(participant1);
        assertTrue(result.contains("information available"));
        assertTrue(result.contains(name));
        assertTrue(result.contains(iban));
        assertTrue(result.contains(bic));
    }

    /***
     * Checks if a Participant with missing bank details
     * has their (unavailable) payment information generated properly
     */
    @Test
    void bankMissing(){
        String name = "Vox V.";
        String iban = "";
        String bic = "ABNANL2AXXX";

        participant1.setLegalName(name);
        participant1.setIban(iban);
        participant1.setBic(bic);

        var result = sut.getBankDetails(participant1);
        assertTrue(result.contains("information unavailable"));
        assertTrue(result.contains(name));
        assertTrue(result.contains("(MISSING)"));
        assertTrue(result.contains(bic));
    }
}