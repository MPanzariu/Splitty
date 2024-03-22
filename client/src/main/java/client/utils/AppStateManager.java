package client.utils;

import com.google.inject.Inject;
import commons.Event;


public class AppStateManager {
    private WebSocketUtils socketUtils;

    @Inject
    public AppStateManager(WebSocketUtils socketUtils) {
        this.socketUtils = socketUtils;
    }

    public void test(String eventID){
        System.out.println("Yup, this method gets called!");
        String url = "/topic/events/" + eventID;
        socketUtils.registerForMessages((event) ->{
            System.out.println(event.toString());
        }, url, Event.class);
    }
}
