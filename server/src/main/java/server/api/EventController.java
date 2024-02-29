package server.api;
import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    private EventRepository repository;
    /**
     * Constructor of EventController.
     * @param eventService the EventService used for backend handling of events
     * @param repository the EventRepository storing Events
     */
    @Autowired
    public EventController(EventService eventService, EventRepository repository) {
        this.eventService = eventService;
        this.repository = repository;
    }

    /**
     * Updates the title of the event
     * @param eventId identifies the event for which we want to update the title
     * @param newTitle the new title
     * @return returns "ok" if the operation succeeds
     */
    @PutMapping("/events/{eventId}")
    public ResponseEntity<Event> editTitle(@PathVariable String eventId,
                                           @RequestBody String newTitle){
        Event updatedEvent = eventService.editTitle(eventId, newTitle);
        return ResponseEntity.ok(updatedEvent);
    }

    /**
     * Creates a new instance of a participant, which is then tied to an event
     * @param eventId identifies the id for which we want to add a participant
     * @param participantName the name of the participant we will add
     * @return returns the status of the operation
     */
    @PostMapping("/{eventId}")
    public ResponseEntity<Void> addParticipantToEvent(@PathVariable String eventId, @RequestBody
        String participantName) {
        eventService.addParticipantToEvent(participantName, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Endpoint for adding an event. Title needs to be a valid, and any other field gets ignored.
     * @param eventName - the name of the Event to be created
     * @return The added event iff the title is valid. Else return a bad request.
     */
    @PostMapping("/")
    ResponseEntity<Event> add(@RequestBody String eventName) {
        if(eventName==null || eventName.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        Event createdEvent = eventService.createEvent(eventName);
        return ResponseEntity.ok(createdEvent);
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

    /***
     * For the purpose of placing a TestEventRepository in the tests
     * @param repository - the TestEventRepository
     */
    public void setRepository(EventRepository repository) {
        this.repository = repository;
    }
}