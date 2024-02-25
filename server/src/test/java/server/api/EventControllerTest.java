package server.api;

import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import server.database.EventRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EventControllerTest {

    EventRepository repository = mock(EventRepository.class);
    EventController controller;

    @BeforeEach
    void setup() {
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
        when(repository.save(event)).thenReturn(event);
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
        String invitationCode = "Invitation to party";
        Event event = new Event("Party");
        when(repository.findById(invitationCode)).thenReturn(Optional.of(event));
        controller.add(event);
        ResponseEntity<Event> response = controller.join(invitationCode);
        assertEquals(event, response.getBody());
    }
}
