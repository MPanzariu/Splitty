package server.api;
import commons.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
    private final ParticipantService participantService;

    /**
     * constructor
     * @param participantService sets the current eventService
     */
    @Autowired
    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }
    /**
     * Update the details of a participant associated with a specific event
     * in a RESTful way
     * @param participantId the participant whose details we want to change
     * @param participantDetails the details of the participant that we want to change
     * @return whether this worked (gives ok) or not
     */
    @PutMapping("/participants/{participantId}")
    public ResponseEntity<Participant> editParticipant(@PathVariable Long participantId,
                                                       @RequestBody Participant participantDetails) {
        Participant updatedParticipant = participantService.editParticipant(participantId, participantDetails);
        return ResponseEntity.ok(updatedParticipant);
    }

    /**
     * handles the DELETE request in a RESTful way
     * @param participantId the eliminated participant
     * @param eventId ID of the event the participant is part of
     * @return a response entity object with a HTTP 200 OK status
     */
    @DeleteMapping("/{eventId}/{participantId}")
    public ResponseEntity<?> removeParticipant(@PathVariable String eventId, @PathVariable Long participantId) {
        participantService.removeParticipant(eventId, participantId);
        return ResponseEntity.ok().build();
    }
}