package client.utils;

import client.scenes.StartupScreenCtrl;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;
import java.util.List;

public class MultiEventStateManager {
    private StartupScreenCtrl startupScreen;
    private final WebSocketUtils socketUtils;
    private final ServerUtils server;
    private StompSession.Subscription currentNameSubscription;
    private StompSession.Subscription currentDeletionSubscription;
    private List<String> relevantEvents;

    /***
     * Constructor for the MESManager
     * @param socketUtils the WebSocketUtils to use
     * @param server the ServerUtils to use
     */
    public MultiEventStateManager(WebSocketUtils socketUtils, ServerUtils server) {
        this.socketUtils = socketUtils;
        this.server = server;
        this.relevantEvents = new ArrayList<>(5);
    }

    /***
     * Register for messages of deletion and name changes
     */
    public void subscribeToUpdates(){
        currentDeletionSubscription = socketUtils.registerForMessages(this::onDeletion, "/topic/events/deletions", String.class);
        currentNameSubscription = socketUtils.registerForMessages(this::onNameChange, "/topic/events/names", Pair.class);
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
     * @param eventId the ID of the deleted event
     */
    public void onDeletion(String eventId){
        if(relevantEvents.contains(eventId)){
            //Tell controller to remove item
            //Switch back to main screen if we have it active right now
        }
    }
    /***
     * Runs on name change of an event
     * @param eventAndName a Pair of the event ID and new name
     */
    public void onNameChange(Pair<String, String> eventAndName){
        String eventId = eventAndName.getKey();
        if(relevantEvents.contains(eventAndName.getKey())){
            String eventName = eventAndName.getValue();
            //Tell controller to change name
        }
    }
}
