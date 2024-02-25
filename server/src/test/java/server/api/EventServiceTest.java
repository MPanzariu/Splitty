//package server.api;

//import commons.Event;
//import commons.Participant;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import server.database.EventRepository;
//import server.database.ParticipantRepository;
//
//import java.util.Optional;
//import java.util.Set;

//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
//public class EventServiceTest {

//    @Mock
//    private EventRepository mockEventRepository;
//
//    @Mock
//    private ParticipantRepository mockParticipantRepository;

//    @InjectMocks
//    private EventService mockEventService;

//    /**
//     * tests if the title editing method works, on an existing event
//     */
//    @Test
//    public void editTitleExistingTest() {
//        Event event = new Event("title", null);
//        when(mockEventRepository.findById(anyString())).
//                thenReturn(Optional.of(event));
//        Event newEvent = mockEventService.
//                editTitle(event.getId(), "newTitle");
//        assertEquals(newEvent.getTitle(), "newTitle");
//    }

//    /**
//     * tests editing a title on an event which does not exist
//     */
//   @Test
//    public void editTitleNotExistingTest(){
//        Event event = new Event("title", null);
//        assertThrows(EntityNotFoundException.class, () ->
//            mockEventService.editTitle(event.getId(), "new title"));
//    }

//    /**
//     * tests whether adding a participant to an existing event works
//     */
//    @Test
//    public void addParticipantsTest(){
//        Event event = new Event("test", null);
//        Participant participant = new Participant();
//        when(mockEventRepository.findById(anyString())).
//                thenReturn(Optional.of(event));
//        when(mockParticipantRepository.save(any(Participant.class)))
//                .thenReturn(participant);
//        mockEventService.addParticipantToEvent("Name Surname", event.getId());
//        Set<Participant> participants = event.getParticipants();
//        assertNotNull(participants);
//    }


//
//}
