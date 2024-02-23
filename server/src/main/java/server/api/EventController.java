package server.api;
import org.springframework.beans.factory.annotation.Autowired;
import commons.Expense;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    /**
     * constructor
     * @param eventService sets the current eventService
     */
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

}