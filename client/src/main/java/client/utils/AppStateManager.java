package client.utils;

import client.scenes.SimpleRefreshable;
import client.scenes.StartupScreenCtrl;
import com.google.inject.Inject;
import commons.Event;
import commons.dto.EventDeletedDTO;
import commons.dto.EventNameChangeDTO;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.*;


public class AppStateManager {
    private final WebSocketUtils socketUtils;
    private final ServerUtils server;
    private StompSession.Subscription currentClientSubscription;
    private HashMap<Class<?>, SimpleRefreshable> controllerMap;
    private SimpleRefreshable currentlyOpen;
    private Event event;
    private StartupScreenCtrl startupScreen;
    private final Set<String> relevantEvents;

    /***
     * Constructor for the AppStateManager
     * @param socketUtils the WebSocketUtils to use
     * @param server the ServerUtils to use
     */
    @Inject
    public AppStateManager(WebSocketUtils socketUtils, ServerUtils server) {
        this.socketUtils = socketUtils;
        socketUtils.startConnection();
        this.server = server;
        this.currentClientSubscription = null;
        this.currentlyOpen = null;
        this.event = null;
        this.relevantEvents = new HashSet<>(5);
    }

    /***
     * Runs any time the Event is updated from the backend
     * @param event the new Event
     */
    public void onEventUpdate(Event event){
        this.event = event;
        if(currentlyOpen!=null && currentlyOpen.shouldLiveRefresh())
            currentlyOpen.refresh(this.event);
    }

    /***
     * Runs when the client changes the Event being observed
     * @param eventID the ID of the new Event
     */
    public void switchClientEvent(String eventID){
        Event newEvent = server.getEvent(eventID);
        onEventUpdate(newEvent);
        if(currentClientSubscription!=null) currentClientSubscription.unsubscribe();
        String url = "/topic/events/" + eventID;
        this.currentClientSubscription = socketUtils.registerForMessages(this::onEventUpdate,
                url, Event.class);
    }

    /***
     * Runs when switching screens in the client
     * @param target the Class of the new screen controller
     */
    public void onSwitchScreens(Class<?> target) {
        SimpleRefreshable controller = controllerMap.get(target);
        controller.refresh(this.event);
        this.currentlyOpen = controller;
    }

    /***
     * Sets the map of Classes to their instances (all under one interface)
     * @param controllerMap the map of Controller Classes to their SimpleRefreshable instances
     */
    public void setControllerMap(HashMap<Class<?>, SimpleRefreshable> controllerMap){
        this.controllerMap = controllerMap;
    }

    /***
     * Register for messages of deletion and name changes
     */
    public void subscribeToUpdates(){
        socketUtils.registerForMessages(this::onDeletion, "/topic/events/deletions", EventDeletedDTO.class);
        socketUtils.registerForMessages(this::onNameChange, "/topic/events/names", EventNameChangeDTO.class);
    }

    /***
     * Sets the StartupScreenCtrl to use
     * @param startupScreen the StartupScreenCtrl to call with updates
     */
    public void setStartupScreen(StartupScreenCtrl startupScreen){
        this.startupScreen = startupScreen;
    }

    /***
     * Adds a subscribed to event
     * @param eventId the ID of the event
     */
    public void addSubscription(String eventId){
        relevantEvents.add(eventId);
    }
    /***
     * Removes a subscribed to event
     * @param eventId the ID of the event
     */
    public void removeSubscription(String eventId){
        relevantEvents.remove(eventId);
    }

    /***
     * Runs on deletion of an event
     * @param dto A DTO containing the ID of the deleted event
     */
    public void onDeletion(EventDeletedDTO dto){
        String eventId = dto.getEventId();
        if(relevantEvents.contains(eventId)){
            startupScreen.removeFromHistoryIfExists(eventId);
            //Switch back to main screen if we have it active right now
        }
    }
    /***
     * Runs on name change of an event
     * @param dto a DTO containing the event ID and new name
     */
    public void onNameChange(EventNameChangeDTO dto){
        String eventId = dto.getEventId();
        if(relevantEvents.contains(eventId)){
            String eventName = dto.getNewTitle();
            startupScreen.addToHistory(eventId, eventName);
        }
    }
}
