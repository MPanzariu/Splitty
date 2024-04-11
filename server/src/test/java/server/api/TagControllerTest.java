package server.api;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.websockets.WebSocketService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
public class TagControllerTest {
    @Mock
    private TagService tagService;

    @Mock
    private WebSocketService webSocketService;

    @InjectMocks
    private TagController tagController;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    @BeforeEach
    void setup() {
    }

    @Test
    void addTagToEventSuccess() {
        String eventId = "event1";
        String tagName = "urgent";
        String colorCode = "#FF0000";

        doNothing().when(tagService).addTagToEvent(anyString(), anyString(), anyString());
        doNothing().when(webSocketService).propagateEventUpdate(anyString());

        ResponseEntity<Void> response = tagController.addTagToEvent(eventId, tagName, colorCode);

        verify(tagService).addTagToEvent(eventId, tagName, colorCode);
        verify(webSocketService).propagateEventUpdate(eventId);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
