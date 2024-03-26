package client.utils;

import client.scenes.SimpleRefreshable;
import commons.Event;

import java.util.ArrayList;
import java.util.List;

public class TestRefreshable implements SimpleRefreshable {
    private Event currentEvent = null;
    private final List<Event> eventsRefreshed = new ArrayList<>();
    private boolean shouldLiveRefresh = true;

    @Override
    public void refresh(Event event) {
        currentEvent = event;
        eventsRefreshed.add(event);
    }

    @Override
    public boolean shouldLiveRefresh() {
        return shouldLiveRefresh;
    }

    public void setShouldLiveRefresh(boolean shouldLiveRefresh) {
        this.shouldLiveRefresh = shouldLiveRefresh;
    }

    public List<Event> getEventsRefreshed() {
        return eventsRefreshed;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }
}
