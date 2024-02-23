package server.api;

import commons.Event;
import commons.Expense;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;

import java.util.List;
@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final EventRepository eventRepository;

    /**
     *
     * @param expenseRepository
     * @param eventRepository
     */
    public ExpenseService(ExpenseRepository expenseRepository, EventRepository eventRepository) {
        this.expenseRepository = expenseRepository;
        this.eventRepository = eventRepository;
    }

    public void addExpenseToEvent(Long eventId, Expense expense) {
        Event event = eventRepository.findById(eventId).orElseThrow(()
            -> new IllegalArgumentException("Event not found"));
        expense.setEvent(event);
        expenseRepository.save(expense);
    }

    public List<Expense> getAllExpensesForEvent(Long eventId) {
        return expenseRepository.findByEventId(eventId);
    }

    public int calculateTotalExpensesForEvent(Long eventId) {
        List<Expense> expenses = getAllExpensesForEvent(eventId);
        return expenses.stream().mapToInt(Expense::getPriceInCents).sum();
    }
}
