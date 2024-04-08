package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.websockets.WebSocketService;

@RestController
@RequestMapping("/api/events")
public class TagController {
    private final TagService tagService;
    private final WebSocketService webSocketService;

    /**
     * Constructor
     * @param tagService TagService to use
     * @param webSocketService WebSocketService to use
     */
    @Autowired
    public TagController(TagService tagService, WebSocketService webSocketService){
        this.tagService = tagService;
        this.webSocketService = webSocketService;
    }

    /**
     * Adds a tag to an event
     * @param eventId id of the event to add the tag to
     * @param tagName name of the tag
     * @param colorCode color code of the tag
     * @return ResponseEntity of the request
     */
    @PostMapping("/{eventId}/tag/{tagName}")
    public ResponseEntity<Void> addTagToEvent(@PathVariable String eventId, @PathVariable String tagName,
                                              @RequestBody String colorCode){
        tagService.addTagToEvent(eventId, tagName, colorCode);
        webSocketService.propagateEventUpdate(eventId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Removes a tag from an event
     * @param eventId id of the event to remove the tag from
     * @param tagId id of the tag to remove
     * @return ResponseEntity of the request
     */
    @DeleteMapping("/{eventId}/tag/{tagId}")
    public ResponseEntity<?> removeTag(@PathVariable String eventId,
                                       @PathVariable Long tagId){
        tagService.removeTag(eventId, tagId);
        webSocketService.propagateEventUpdate(eventId);
        return ResponseEntity.ok().build();
    }
}
