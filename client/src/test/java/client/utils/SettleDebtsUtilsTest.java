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

    @Test
    void createSettlementExpense() {
        var result = sut.createSettlementExpense(participant1, 7, participant2);
        assertNotNull(result);
    }

    @Test
    void createSettleAction() {
        EventHandler<ActionEvent> result = sut.createSettleAction(participant1, 7, participant2, "ABC123");
        result.handle(new ActionEvent());
        assertNotNull(result);
    }

    @Test
    void stringPositive() {
        var result = sut.createDebtString(participant1, 7, participant2);
        assertTrue(result.contains(participant1.getName()));
        assertTrue(result.contains("is owed"));
        assertTrue(result.contains("0.07"));
    }

    @Test
    void stringNegative() {
        var result = sut.createDebtString(participant1, -7, participant2);
        assertTrue(result.contains(participant1.getName()));
        assertTrue(result.contains("owes"));
        assertTrue(result.contains("0.07"));
    }

    @Test
    void stringZero() {
        var result = sut.createDebtString(participant1, 0, participant2);
        assertTrue(result.contains(participant1.getName()));
        assertTrue(result.contains("owes"));
        assertFalse(result.contains("to"));
    }
}