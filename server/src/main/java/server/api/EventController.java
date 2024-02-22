package server.api;
import commons.Expense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    //Empty event controller class
    private ExpenseService expenseService;
    @PostMapping("/{eventId}/expenses")
    public ResponseEntity<Void> addExpenseToEvent(@PathVariable Long eventId, @RequestBody
    Expense expense) {
        expenseService.addExpenseToEvent(eventId, expense);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/{eventId}/expenses")
    public ResponseEntity<List<Expense>> getAllExpensesForEvent(@PathVariable Long eventId) {
        List<Expense> expenses = expenseService.getAllExpensesForEvent(eventId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{eventId}/total-expenses")
    public ResponseEntity<Double> calculateTotalExpensesForEvent(@PathVariable Long eventId) {
        double totalExpenses = expenseService.calculateTotalExpensesForEvent(eventId);
        return ResponseEntity.ok(totalExpenses);
    }
}