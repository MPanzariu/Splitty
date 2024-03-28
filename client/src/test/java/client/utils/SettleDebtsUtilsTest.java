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

    @BeforeEach
    void setup(){
        participant1 = new Participant("Vox");
        participant2 = new Participant("Val");
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