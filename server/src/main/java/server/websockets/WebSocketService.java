package server.websockets;

import commons.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate socketMessenger;

    /***
     * Basic WebSocketService constructor
     * @param socketMessenger the template converting changes into WebSocket messages
     */
    @Autowired
    public WebSocketService(SimpMessagingTemplate socketMessenger) {
        this.socketMessenger = socketMessenger;
    }

    /***
     * Propagates changes to an Event to all WebSocket Clients
     * @param event the new Event, to be sent to clients
     */
    public void propagateEventUpdate(Event event){
        socketMessenger.convertAndSend(eventUpdateURL(event.getId()), event);
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
