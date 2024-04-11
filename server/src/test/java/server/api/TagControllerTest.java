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
    @Test
    void addTagToEventFail() {
        String eventId = "event1";
        String tagName = "urgent";
        String colorCode = "#FF0000";

        doThrow(new IllegalArgumentException()).when(tagService).addTagToEvent(anyString(), anyString(), anyString());

        assertThrows(IllegalArgumentException.class, () -> {
            tagController.addTagToEvent(eventId, tagName, colorCode);
        });
    }

    @Test
    void removeTagSuccess() {
        String eventId = "event1";
        Long tagId = 1L;

        doNothing().when(tagService).removeTag(anyString(), anyLong());
        doNothing().when(webSocketService).propagateEventUpdate(anyString());

        ResponseEntity<?> response = tagController.removeTag(eventId, tagId);

        verify(tagService).removeTag(eventId, tagId);
        verify(webSocketService).propagateEventUpdate(eventId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void removeTagFail() {
        String eventId = "event1";
        Long tagId = 1L;

        doThrow(new IllegalArgumentException()).when(tagService).removeTag(anyString(), anyLong());

        assertThrows(IllegalArgumentException.class, () -> {
            tagController.removeTag(eventId, tagId);
        });
    }
}
