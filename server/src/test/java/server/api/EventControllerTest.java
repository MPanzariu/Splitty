package server.api;

import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.database.EventRepository;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EventControllerTest {

    EventRepository repository;
    EventController controller;

    @BeforeEach
    void setup() {
        repository = new TestEventRepository();
        controller = new EventController(null, repository);
    }

    @Test
    void addEventTitleIsNull() {
        Event event = new Event(null, null);
        ResponseEntity<Event> response = controller.add(event);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addEventEmptyTitle() {
        Event event = new Event("", null);
        ResponseEntity<Event> response = controller.add(event);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addValidEvent() {
        Event event = new Event("Party", null);
        ResponseEntity<Event> response = controller.add(event);
        assertEquals(event, response.getBody());
    }

    @Test
    void joinNonExistingEvent() {
        String invitationCode = "unkown entity";
        ResponseEntity<Event> response = controller.join(invitationCode);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void joinExistingEvent() {
        Event event = new Event("Party", null);
        Event persistedEvent = controller.add(event).getBody();
        ResponseEntity<Event> response = controller.join(persistedEvent.getId());
        assertEquals(event, response.getBody());
    }

    @Test
    void getMultipleEvents() {
        Event event1 = new Event("Party", null);
        Event event2 = new Event("Holiday", null);
        Event expectedEvent1 = controller.add(event1).getBody();
        Event expectedEvent2 = controller.add(event2).getBody();
        List<Event> expectedEvents = List.of(expectedEvent1, expectedEvent2);
        List<Event> retrievedEvents = controller.all().getBody();
        assertEquals(expectedEvents, retrievedEvents);
    }

    @Test
    void removeNonExistingEvent() {
        Event event = new Event("Party", null);
        controller.add(event);
        String invitationCode = "Fake invitation code";
        ResponseEntity<Event> response = controller.remove(invitationCode);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void removeExistingEvent() {
        Event event = new Event("Party", null);
        Event persistedEvent = controller.add(event).getBody();
        ResponseEntity<Event> response = controller.remove(persistedEvent.getId());
        assertEquals(event, response.getBody());
    }

    @Test
    void orderUnorderedListByTitle() {
        Event event1 = new Event("B", null);
        Event persistedEvent1 = controller.add(event1).getBody();
        Event event2 = new Event("A", null);
        Event persistedEvent2 = controller.add(event2).getBody();
        ResponseEntity<List<Event>> response = controller.orderByTitle();
        List<Event> orderedList = List.of(persistedEvent2, persistedEvent1);
        assertEquals(orderedList, response.getBody());
    }

    @Test
    void orderUnorderedListByCreationDate() {
        Event event1 = new Event("B", new Date(2L));
        Event persistedEvent1 = controller.add(event1).getBody();
        Event event2 = new Event("A", new Date(1L));
        Event persistedEvent2 = controller.add(event2).getBody();
        ResponseEntity<List<Event>> response = controller.orderByCreationDate();
        List<Event> orderedList = List.of(persistedEvent2, persistedEvent1);
        assertEquals(orderedList, response.getBody());
    }
}
