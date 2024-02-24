package server.api;

import commons.Event;
import commons.Expense;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.ExpenseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {
    @Mock
    private ExpenseRepository mockExpenseRepository;

    @InjectMocks
    private ExpenseService mockExpenseService;

    @Test
    public void getAllExpensesForEventTest() {
        String eventId = "mockEventId";
        List<Expense> expectedExpenses = new ArrayList<>();
        expectedExpenses.add(new Expense("Expense 1", 100,
            null, null, null));
        expectedExpenses.add(new Expense("Expense 2", 200,
            null, null, null));

        // Stubbing behavior of mockExpenseRepository
        when(mockExpenseRepository.findByEventId(anyString()))
            .thenReturn(expectedExpenses);

        // Call the method under test
        List<Expense> actualExpenses =
            mockExpenseService.getAllExpensesForEvent(eventId);

        // Verify the result
        assertEquals(expectedExpenses, actualExpenses);
    }

    @Test
    public void calculateTotalExpensesForEventTest() {
        String eventId = "mockEventId";
        List<Expense> expectedExpenses = new ArrayList<>();
        expectedExpenses.add(new Expense("Expense 1", 100,
            null, null, null));
        expectedExpenses.add(new Expense("Expense 2", 200,
            null, null, null));
        when(mockExpenseRepository.findByEventId(anyString()))
            .thenReturn(expectedExpenses);
        List<Expense> actualExpenses = mockExpenseService
            .getAllExpensesForEvent(eventId);
        int sum = 0;
        for (Expense actualExpense : actualExpenses)
            sum += actualExpense.getPriceInCents();
        assertEquals(sum, 300);
    }

    @Test
    public void deleteExpenseTest() {
        Event mockEvent = new Event("Sample Event");
        Expense mockExpense = new Expense("mockExpense", 100,
            null, mockEvent, null);
        mockEvent.addExpense(mockExpense);
        when(mockExpenseRepository.findById(anyLong()))
            .thenReturn(Optional.of(mockExpense));
        mockExpenseService.deleteExpense(mockExpense.getId());
        assertFalse(mockEvent.getExpenses().contains(mockExpense));
    }
}