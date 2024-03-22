package server.websockets;

import commons.Event;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import server.database.EventRepository;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate socketMessenger;
    private final EventRepository eventRepository;

    /***
     * Basic WebSocketService constructor
     * @param socketMessenger the template converting changes into WebSocket messages
     * @param eventRepository the EventRepository to fetch data from
     */
    @Autowired
    public WebSocketService(SimpMessagingTemplate socketMessenger,
                            EventRepository eventRepository) {
        this.socketMessenger = socketMessenger;
        this.eventRepository = eventRepository;
    }

    /***
     * Propagates changes to an Event to all WebSocket Clients
     * @param eventID the ID of the updated Event
     */
    public void propagateEventUpdate(String eventID){
        Event updatedEvent = eventRepository.findById(eventID)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        socketMessenger.convertAndSend(eventUpdateURL(eventID), updatedEvent);
    }

    /***
     * Generates the URL to send updates for a specific event
     * @param eventID the ID of the event clients subscribed to
     * @return the WebSocket URL to send updates to
     */
    private String eventUpdateURL(String eventID){
        return "/topic/events/" + eventID;
    }
}
