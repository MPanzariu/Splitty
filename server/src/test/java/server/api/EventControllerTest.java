package server.api;

import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.database.EventRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EventControllerTest {

    EventRepository repository;
    EventController controller;

    @BeforeEach
    void setup() {
        repository = new TestEventRepository();
        EventService eventService = new EventService(repository, null);
        controller = new EventController(eventService, repository);
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
        controller.add("Party");
        String invitationCode = "Fake invitation code";
        ResponseEntity<Event> response = controller.remove(invitationCode);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void removeExistingEvent() {
        Event persistedEvent = controller.add("Party").getBody();
        assertNotNull(persistedEvent);
        ResponseEntity<Event> response = controller.remove(persistedEvent.getId());
        assertEquals(persistedEvent, response.getBody());
    }

    @Test
    void orderUnorderedListByTitle() {
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
        Event persistedEvent1 = controller.add("First").getBody();
        Event persistedEvent2 = controller.add("Second").getBody();
        assertNotNull(persistedEvent1);
        assertNotNull(persistedEvent2);
        ResponseEntity<List<Event>> response = controller.orderByCreationDate();
        List<Event> orderedList = List.of(persistedEvent1, persistedEvent2);
        assertEquals(orderedList, response.getBody());
    }
}
