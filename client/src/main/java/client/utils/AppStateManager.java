package client.utils;

import client.scenes.SimpleRefreshable;
import com.google.inject.Inject;
import commons.Event;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.HashMap;


public class AppStateManager {
    private final WebSocketUtils socketUtils;
    private final ServerUtils server;
    private StompSession.Subscription currentClientSubscription;
    private HashMap<Class<?>, SimpleRefreshable> controllerMap;
    private SimpleRefreshable currentlyOpen;
    private Event event;

    /***
     * Constructor for the AppStateManager
     * @param socketUtils the WebSocketUtils to use
     * @param server the ServerUtils to use
     */
    @Inject
    public AppStateManager(WebSocketUtils socketUtils, ServerUtils server) {
        this.socketUtils = socketUtils;
        this.server = server;
        this.currentClientSubscription = null;
        this.currentlyOpen = null;
        this.event = null;
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
}
