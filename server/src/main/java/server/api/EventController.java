package server.api;
import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    private final EventRepository repository;

    /**
     * constructor
     * @param eventService sets the current eventService
     */
    @Autowired
    public EventController(EventService eventService, EventRepository repository) {
        this.eventService = eventService;
        this.repository = repository;
    }

    /**
     * Endpoint for adding an event. Title needs to be a valid, and any other field gets ignored.
     * @param event Event that is to be added to the database.
     * @return The added event iff the title is valid. Else return a bad request.
     */
    @PostMapping("/add")
    ResponseEntity<Event> add(@RequestBody Event event) {
        if(event.getTitle() == null || event.getTitle().isEmpty())
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(repository.save(event));
    }

    /**
     * Endpoint for joining an event.
     * @param id ID of the event
     * @return ResponseEntity with the event iff the event can be found. Else return a bad request.
     */
    @GetMapping("join/{id}")
    ResponseEntity<Event> join(@PathVariable() String id) {
        Optional<Event> event = repository.findById(id);
        if(event.isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(event.get());
    }
}