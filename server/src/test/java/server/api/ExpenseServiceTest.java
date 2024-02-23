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
        mockEvent = new Event("Sample Event", "CODE123");
        Expense expense = new Expense("Sample Expense", 100,
            null, mockEvent, mockParticipant);
        mockExpenseService.addExpenseToEvent(mockEvent.getId(), expense);
        when(mockEventRepository.findById(anyLong())).thenReturn(Optional.of(mockEvent));
        // Assert
        assertEquals(mockEvent, expense.getEvent());
    }

    @Test
    void getAllExpensesForEvent() {
    }

    @Test
    void calculateTotalExpensesForEvent() {
    }
}