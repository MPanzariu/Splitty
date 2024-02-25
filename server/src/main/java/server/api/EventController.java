package server.api;
import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import commons.Expense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    private final EventRepository eventRep;

    /**
     * constructor
     * @param eventService sets the current eventService
     */
    @Autowired
    public EventController(EventService eventService, EventRepository eventRep, ParticipantRepository participantRep, //
                           ExpenseRepository expenseRep) {
        this.eventService = eventService;
        this.eventRep = eventRep;
    }

    /**
     * Endpoint for adding an event. Title needs to be a valid, and any other field gets ignored.
     * @param event Event that is to be added to the database.
     * @return The added event.
     */
    @PostMapping("/add")
    ResponseEntity<Event> addEvent(@RequestBody Event event) {
        if(event.getTitle() == null || event.getTitle().isEmpty())
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(eventRep.save(event));
    }
}