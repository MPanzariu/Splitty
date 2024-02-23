package server.api;
import commons.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import commons.Expense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    /**
     * constructor
     * @param eventService sets the current eventService
     */
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * handles the DELETE request in a RESTful way
     * @param eventId the event we want to delete a participant from
     * @param participantId the eliminated participant
     * @return a response entity object with a HTTP 200 OK status
     */
    @DeleteMapping("/{eventId}/participants/{participantId}")
    public ResponseEntity<?> removeParticipant(@PathVariable Long eventId, @PathVariable Long participantId) {
        eventService.removeParticipantFromEvent(eventId, participantId);
        return ResponseEntity.ok().build();
    }

    //Empty event controller class
    private ExpenseService expenseService;

    /**
     * Method responsible for the post command. It is used to update a specific
     * event expenses
     * @param eventId identifies the even by the specific ID
     * @param expense maps the expense to the specific even specified by the ID
     * @return the status of the task
     */
    @PostMapping("/{eventId}/expenses")
    public ResponseEntity<Void> addExpenseToEvent(@PathVariable Long eventId, @RequestBody
    Expense expense) {
        expenseService.addExpenseToEvent(eventId, expense);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * The method is responsible for creating a GET request
     * that returns the desired information
     * @param eventId identifies the even by the specific ID
     * @return the status of the specific page and the information
     * (a list of all expenses for a specific event)
     */
    @GetMapping("/{eventId}/expenses")
    public ResponseEntity<List<Expense>> getAllExpensesForEvent(@PathVariable Long eventId) {
        List<Expense> expenses = expenseService.getAllExpensesForEvent(eventId);
        return ResponseEntity.ok(expenses);
    }

    /**
     * The method is responsible for creating a GET request
     * that returns the desired information
     * @param eventId identifies the even by the specific ID
     * @return the sum of all the expenses for a specific event
     */
    @GetMapping("/{eventId}/total-expenses")
    public ResponseEntity<Double> calculateTotalExpensesForEvent(@PathVariable Long eventId) {
        double totalExpenses = expenseService.calculateTotalExpensesForEvent(eventId);
        return ResponseEntity.ok(totalExpenses);
    }

    /**
     * Update the details of a participant associated with a specific event
     * in a RESTful way
     * @param eventCode the event we are looking into
     * @param participantCode the participant whose details we want to change
     * @param participantDetails the details of the participant that we want to change
     * @return whether this worked (gives ok) or not
     */
    @PutMapping("/{eventCode}/participants/{participantCode}")
    public ResponseEntity<Participant> editParticipant(@PathVariable String eventCode, @PathVariable String participantCode,
                                                       @RequestBody Participant participantDetails) {
        Participant updatedParticipant = eventService.editParticipantToEvent(eventCode, participantCode, participantDetails);
        return ResponseEntity.ok(updatedParticipant);
    }
}