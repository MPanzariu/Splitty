package server.api;
import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}