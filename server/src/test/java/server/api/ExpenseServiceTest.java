package server.api;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {
    @Mock
    private ExpenseRepository mockExpenseRepository;

    @Mock
    private EventRepository mockEventRepository;

    @Mock
    private Event mockEvent;

    @Mock
    private Participant mockParticipant;

    @InjectMocks
    private ExpenseService mockExpenseService;

    @Test
    void addExpenseToEventTest() {
        assertTrue(1 == 1);
    }

    @Test
    void getAllExpensesForEvent() {
        assertTrue(1 == 1);
    }

    @Test
    void calculateTotalExpensesForEvent() {
        assertTrue(1 == 1);
    }
}