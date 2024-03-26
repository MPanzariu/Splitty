package client.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebSocketUtilsTest {

    @Test
    void getLoggingSessionHandlerAdapter() {
        WebSocketUtils sut = new WebSocketUtils("/test");
        var adapter = sut.getLoggingSessionHandlerAdapter();
        assertNotNull(adapter);
        adapter.handleException(null, null, null, null, new Exception());
        adapter.handleTransportError(null, new Exception());
    }
}