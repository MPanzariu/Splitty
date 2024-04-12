package server.api;

import commons.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.websockets.WebSocketService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class TagControllerTests {
    private TagService tagService;
    private WebSocketService webSocketService;
    private TagController controller;

    /**
     * Test setup
     */
    @BeforeEach
    public void setup() {
        tagService = mock(TagService.class);
        webSocketService = mock(WebSocketService.class);
        controller = new TagController(tagService, webSocketService);
    }

    /**
     * A 200 OK response should be sent after a tag, already existing in the database, is edited.
     */
    @Test
    public void editExistingTag() {
        String eventId = "Holiday";
        Long tagId = 5L;
        Tag newTag = new Tag("Food", "#FFFFFF");
        ResponseEntity<Tag> response = controller.editTag(eventId, tagId, newTag);
        verify(tagService).editTag(tagId, newTag);
        verify(webSocketService).propagateEventUpdate(eventId);
        assertEquals(OK, response.getStatusCode());
    }

    /**
     * A 400 BAD_REQUEST response should be returned when a tag, that is not in the database, is edited.
     */
    @Test
    public void editNonExistingTag() {
        String eventId = "Holiday";
        Long tagId = 5L;
        Tag newTag = new Tag("Food", "#FFFFFF");
        doThrow(EntityNotFoundException.class).when(tagService).editTag(tagId, newTag);
        ResponseEntity<Tag> response = controller.editTag(eventId, tagId, newTag);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }
}
