package server.api;

import commons.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.websockets.WebSocketService;

@RestController
@RequestMapping("/api/events")
public class TagController {
    private final TagService tagService;
    private final WebSocketService webSocketService;

    @Autowired
    public TagController(TagService tagService, WebSocketService webSocketService){
        this.tagService = tagService;
        this.webSocketService = webSocketService;
    }

    @PostMapping("/{eventId}/tag/{tagName}")
    public ResponseEntity<Void> addTagToEvent(@PathVariable String eventId, @PathVariable String tagName,
                                              @RequestBody String colorCode){
        tagService.addTagToEvent(eventId, tagName, colorCode);
        webSocketService.propagateEventUpdate(eventId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/{eventId}/tag/{tagId}")
    public ResponseEntity<?> removeTag(@PathVariable String eventId,
                                       @PathVariable Long tagId){
        tagService.removeTag(eventId, tagId);
        webSocketService.propagateEventUpdate(eventId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{eventId}/{expenseId}/tag/{tagName}")
    public ResponseEntity<Void> addTagToExpense(@PathVariable String eventId, @PathVariable Long expenseId,
                                                @PathVariable String tagName, @RequestBody String colorCode){
        tagService.addTagToExpense(eventId, expenseId, tagName, colorCode);
        webSocketService.propagateEventUpdate(eventId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping("/{eventId}/{expenseId}/tag/{tagName}")
    public ResponseEntity<Tag> editTagOfExpense(@PathVariable String eventId, @PathVariable Long expenseId,
                                                @PathVariable String tagName, @RequestBody String colorCode){
        Tag tag = tagService.editTagOfExpense(eventId, expenseId, tagName, colorCode);
        webSocketService.propagateEventUpdate(eventId);
        return ResponseEntity.ok(tag);
    }
}
