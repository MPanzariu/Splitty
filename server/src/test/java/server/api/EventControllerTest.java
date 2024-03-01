package server.api;

import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.ResponseEntity;
import server.database.EventRepository;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @Mock
    EventService eventService;
    @InjectMocks
    EventController controller;
    Answer<?> stubCreate;

    @BeforeEach
    void setup() {
        EventRepository repository = new TestEventRepository();
        controller.setRepository(repository);

        stubCreate = (Answer<Event>) invocation -> {
            Date currentDate = new Date();
            Event event = new Event(invocation.getArgument(0), currentDate);
            repository.save(event);
            return event;
        };
    }

    @Test
    void addEventTitleIsNull() {
        ResponseEntity<Event> response = controller.add(null);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addEventEmptyTitle() {
        ResponseEntity<Event> response = controller.add("");
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addValidEvent() {
        when(eventService.createEvent(anyString())).thenAnswer(stubCreate);
        String title = "Party";
        ResponseEntity<Event> response = controller.add(title);
        assertNotNull(response.getBody());
        assertEquals(title, response.getBody().getTitle());
    }

    @Test
    void joinNonExistingEvent() {
        String invitationCode = "unkown entity";
        ResponseEntity<Event> response = controller.join(invitationCode);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void joinExistingEvent() {
        when(eventService.createEvent(anyString())).thenAnswer(stubCreate);
        Event persistedEvent = controller.add("Party").getBody();
        assertNotNull(persistedEvent);
        ResponseEntity<Event> response = controller.join(persistedEvent.getId());
        Event responseEvent = response.getBody();
        assertNotNull(responseEvent);
        assertEquals(persistedEvent.getTitle(), responseEvent.getTitle());
        assertEquals(persistedEvent.getId(), responseEvent.getId());
    }

    @Test
    void getMultipleEvents() {
        when(eventService.createEvent(anyString())).thenAnswer(stubCreate);
        Event expectedEvent1 = controller.add("Party").getBody();
        Event expectedEvent2 = controller.add("Holiday").getBody();
        assertNotNull(expectedEvent1);
        assertNotNull(expectedEvent2);
        List<Event> expectedEvents = List.of(expectedEvent1, expectedEvent2);
        List<Event> retrievedEvents = controller.all().getBody();
        assertEquals(expectedEvents, retrievedEvents);
    }

    @Test
    void removeNonExistingEvent() {
        when(eventService.createEvent(anyString())).thenAnswer(stubCreate);
        controller.add("Party");
        String invitationCode = "Fake invitation code";
        ResponseEntity<Event> response = controller.remove(invitationCode);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void removeExistingEvent() {
        when(eventService.createEvent(anyString())).thenAnswer(stubCreate);
        Event persistedEvent = controller.add("Party").getBody();
        assertNotNull(persistedEvent);
        ResponseEntity<Event> response = controller.remove(persistedEvent.getId());
        assertEquals(persistedEvent, response.getBody());
    }

    @Test
    void orderUnorderedListByTitle() {
        when(eventService.createEvent(anyString())).thenAnswer(stubCreate);
        Event persistedEvent1 = controller.add("B").getBody();
        Event persistedEvent2 = controller.add("A").getBody();
        ResponseEntity<List<Event>> response = controller.orderByTitle();
        assertNotNull(persistedEvent1);
        assertNotNull(persistedEvent2);
        List<Event> orderedList = List.of(persistedEvent2, persistedEvent1);
        assertEquals(orderedList, response.getBody());
    }

    @Test
    void orderUnorderedListByCreationDate() {
        when(eventService.createEvent(anyString())).thenAnswer(stubCreate);
        Event persistedEvent1 = controller.add("First").getBody();
        Event persistedEvent2 = controller.add("Second").getBody();
        assertNotNull(persistedEvent1);
        assertNotNull(persistedEvent2);
        ResponseEntity<List<Event>> response = controller.orderByCreationDate();
        List<Event> orderedList = List.of(persistedEvent1, persistedEvent2);
        assertEquals(orderedList, response.getBody());
    }

}
