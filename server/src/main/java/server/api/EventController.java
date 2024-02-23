package server.api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}