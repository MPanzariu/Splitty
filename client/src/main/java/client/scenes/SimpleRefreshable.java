package client.scenes;

import commons.Event;

public interface SimpleRefreshable {
    /***
     * Refreshes the contents of a controller
     * @param event the new Event data to process
     */
    void refresh(Event event);

    /***
     * Specifies if the screen should be live-refreshed
     * @return true if changes should immediately refresh the screen, false otherwise
     */
    boolean shouldLiveRefresh();
}
