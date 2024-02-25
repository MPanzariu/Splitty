package server.api;
import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository repository;

    /**
     * Constructor of EventController.
     */
    @Autowired
    public EventController(EventRepository repository) {
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
    @GetMapping("/join/{id}")
    ResponseEntity<Event> join(@PathVariable String id) {
        Optional<Event> event = repository.findById(id);
        if(event.isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(event.get());
    }

    /**
     * Endpoint for retrieving all events.
     * @return All events from the database.
     */
    @GetMapping("/all")
    ResponseEntity<List<Event>> all() {
        return ResponseEntity.ok(repository.findAll());
    }

    /**
     * Endpoint for removing an event from the database.
     * @param id ID of the to be removed event.
     * @return OK iff event was found in the database. Else a bad request.
     */
    @DeleteMapping("/remove/{id}")
    ResponseEntity<Event> remove(@PathVariable String id) {
        Optional<Event> event = repository.findById(id);
        if(event.isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(event.get());
    }

    /**
     * Endpoint for giving an ordered list of events by title.
     * @return A list of events ordered by title
     */
    @GetMapping("ordered/title")
    ResponseEntity<List<Event>> orderByTitle() {
        List<Event> events = all().getBody();
        events.sort(Comparator.comparing(Event::getTitle));
        return ResponseEntity.ok(events);
    }

    /**
     * Endopoint for giving all events ordered by creation date.
     * @return List of all events ordered by creation date
     */
    @GetMapping("ordered/date")
    ResponseEntity<List<Event>> orderByCreationDate() {
        List<Event> events = all().getBody();
        events.sort(Comparator.comparing(Event::getCreationDate));
        return ResponseEntity.ok(events);
    }
}