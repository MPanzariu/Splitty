package server.api;
import commons.Participant;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Update the details of a participant associated with a specific event
     * in a RESTful way
     * @param eventId the event we are looking into
     * @param participantId the participant whose details we want to change
     * @param participantDetails the details of the participant that we want to change
     * @return whether this worked (gives ok) or not
     */
    @PutMapping("/{eventId}/participants/{participantId}")
    public ResponseEntity<Participant> editParticipant(@PathVariable Long eventId, @PathVariable Long participantId,
                                                       @RequestBody Participant participantDetails) {
        Participant updatedParticipant = eventService.editParticipantToEvent(eventId, participantId, participantDetails);
        return ResponseEntity.ok(updatedParticipant);
    }
}