package server.api;

import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import server.database.EventRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EventControllerTest {

    EventRepository repository;
    EventController controller;

    @BeforeEach
    void setup() {
        repository = new TestEventRepository();
        controller = new EventController(repository);
    }

    @Test
    void addEventTitleIsNull() {
        Event event = new Event(null);
        ResponseEntity<Event> response = controller.add(event);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addEventEmptyTitle() {
        Event event = new Event("");
        ResponseEntity<Event> response = controller.add(event);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addValidEvent() {
        Event event = new Event("Party");
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
        Event event = new Event("Party");
        Event persistedEvent = controller.add(event).getBody();
        ResponseEntity<Event> response = controller.join(persistedEvent.getId());
        assertEquals(event, response.getBody());
    }

    @Test
    void getMultipleEvents() {
        Event event1 = new Event("Party");
        Event event2 = new Event("Holiday");
        Event expectedEvent1 = controller.add(event1).getBody();
        Event expectedEvent2 = controller.add(event2).getBody();
        List<Event> expectedEvents = List.of(expectedEvent1, expectedEvent2);
        List<Event> retrievedEvents = controller.all().getBody();
        assertEquals(expectedEvents, retrievedEvents);
    }
}
