package server.api;
import commons.Expense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}