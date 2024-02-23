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

}
