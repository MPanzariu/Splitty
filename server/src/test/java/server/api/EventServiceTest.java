package server.api;

import commons.Event;
import commons.Participant;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.database.EventRepository;
import server.database.ParticipantRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private EventService eventService;

    private Event mockEvent;
    private Participant mockParticipant;
    private Participant updatedDetails;

    /**
     * create a mock event and participant to see if it can be removed, not needing to interact
     * with a real database
     */
    @Test
    public void testRemoveParticipantFromEvent() {
        Event mockEvent = new Event("Sample Event", "CODE123");
        Participant mockParticipant = new Participant("John Doe", mockEvent);
        mockEvent.addParticipant(mockParticipant);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(mockEvent));
        when(participantRepository.findById(anyLong())).thenReturn(Optional.of(mockParticipant));
        eventService.removeParticipantFromEvent(1L, 1L);
        assertFalse(mockEvent.getParticipants().contains(mockParticipant));
        verify(eventRepository).save(mockEvent);
    }

    /**
     * now we are removing two participants, check if it is works without
     * having to use the actual database
     */
    @Test
    public void testRemoveMultipleParticipantsFromEvent() {
        Event mockEvent = new Event("Sample Event", "CODE123");
        Participant mockParticipant1 = new Participant("Participant One", mockEvent);
        Participant mockParticipant2 = new Participant("Participant Two", mockEvent);
        mockEvent.addParticipant(mockParticipant1);
        mockEvent.addParticipant(mockParticipant2);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(mockEvent));
        when(participantRepository.findById(1L)).thenReturn(Optional.of(mockParticipant1));
        when(participantRepository.findById(2L)).thenReturn(Optional.of(mockParticipant2));
        eventService.removeParticipantFromEvent(1L, 1L);
        eventService.removeParticipantFromEvent(1L, 2L);
        assertFalse(mockEvent.getParticipants().contains(mockParticipant1));
        assertFalse(mockEvent.getParticipants().contains(mockParticipant2));
        verify(eventRepository, times(2)).save(mockEvent);
    }

    /**
     * check if we try to remove a participant from an event that does not exist
     * and throw an exception in that case
     */
    @Test
    public void removeParticipantFromNonexistentEventThrowsException() {
        long nonexistentEventId = 999L;
        long participantId = 1L;
        when(eventRepository.findById(nonexistentEventId)).thenReturn(Optional.empty());
        try {
            eventService.removeParticipantFromEvent(nonexistentEventId, participantId);
            fail("Expected EntityNotFoundException not thrown");
        } catch (EntityNotFoundException ex) {
            assertEquals("Event not found", ex.getMessage());
        }
        verify(eventRepository, never()).save(any(Event.class));
    }

    /**
     * check to see if the editing of the participant works
     */
    @Test
    public void checkEditParticipantToEvent() {
        Event mockEvent = new Event("Sample Event", "CODE123");
        Participant mockParticipant = new Participant("John Doe", mockEvent);
        Participant updatedDetails = new Participant("Jane Doe", mockEvent);
        when(eventRepository.findByCode(mockEvent.getCode())).thenReturn(Optional.of(mockEvent));
        when(participantRepository.findByEventCodeAndName(mockEvent.getCode(), "John Doe")).thenReturn(Optional.of(mockParticipant));
        when(participantRepository.save(any(Participant.class))).thenReturn(updatedDetails);
        Participant result = eventService.editParticipantToEvent(mockEvent.getCode(), "John Doe", updatedDetails);
        assertEquals("Jane Doe", result.getName());
    }

    /**
     * test to see what happens if we try to edit a participant's detail
     * if the event he was assigned to did not exist
     */
    @Test
    public void editParticipantToEventNonExistentEventCheck() {
        String eventCode = "EVT123";
        String participantCode = "PRT456";
        Participant updatedDetails = new Participant("Jane Doe", null);
        when(eventRepository.findByCode(eventCode)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            eventService.editParticipantToEvent(eventCode, participantCode, updatedDetails);
        }, "Should throw EntityNotFoundException for a nonexistent event.");
    }

    /**
     * test to see what would happen if we tried to edit a participant that was not
     * existing in the first place
     */
    @Test
    public void editParticipantToEventNonExistentParticipantCheck() {
        String eventCode = "EVT123";
        String participantName = "Jane Doe";
        Event mockEvent = new Event("Sample Event", eventCode);

        when(eventRepository.findByCode(eventCode)).thenReturn(Optional.of(mockEvent));
        when(participantRepository.findByEventCodeAndName(eventCode, participantName)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> eventService.editParticipantToEvent(eventCode, participantName, new Participant(participantName, mockEvent)));
    }

    /**
     * Test what happens when we try to edit a participant that does not belong to
     * a specific event
     */
    @Test
    public void editParticipantToEventButItDoesNotBelongToEvent() {
        String eventCode = "EVT123";
        String participantName = "John Doe";
        Event mockEvent = new Event("Sample Event", eventCode);
        Participant mockParticipant = new Participant(participantName, mockEvent);
        Participant updatedDetails = new Participant("Jane Doe", mockEvent);
        when(eventRepository.findByCode(eventCode)).thenReturn(Optional.of(mockEvent));
        when(participantRepository.findByEventCodeAndName(eventCode, participantName)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            eventService.editParticipantToEvent(eventCode, participantName, updatedDetails);
        }, "Should throw EntityNotFoundException if the participant is not found in the specified event.");
    }
}
