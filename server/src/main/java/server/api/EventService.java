package server.api;

import commons.Event;
import commons.Participant;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ParticipantRepository;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;

    /**
     * constructor
     * autowired - automatically inject instances of the parameters when creaitng an EventService
     * @param eventRepository used for handling events
     * @param participantRepository used for handling participants
     */
    @Autowired
    public EventService(EventRepository eventRepository, ParticipantRepository participantRepository) {
        this.eventRepository = eventRepository;
        this.participantRepository = participantRepository;
    }

    /**
     * remove a participant, make sure both the event and participants exist or else
     * throw an exception
     * @param eventId the event we want to remove a participant from
     * @param participantId the participant we want to remove
     */
    public void removeParticipantFromEvent(Long eventId, Long participantId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException("Participant not found"));

        event.getParticipants().remove(participant);
        eventRepository.save(event);
    }

    /**
     * edit the details of a participant that is in an event
     * @param eventId the event where the participant that we want to modify the details of is in
     * @param participantId the participant whose details we want to change
     * @param participantDetails the details of the participant
     * @return the participants modified details are now saved in the database
     */
    public Participant editParticipantToEvent(Long eventId, Long participantId, Participant participantDetails) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException("Participant not found"));
        if (!event.getParticipants().contains(participant)) {
            throw new IllegalArgumentException("Participant does not belong to the specified event");
        }
        participant.setName(participantDetails.getName());
        return participantRepository.save(participant);
    }
}
