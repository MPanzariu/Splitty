package client.utils;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class WebSocketUtils {
    private String serverURL;
    private StompSession session;


    /***
     * WebSocketUtils constructor
     * @param serverURL the server URL to use (injected)
     */
    @Inject
    public WebSocketUtils(@Named("connection.URL") String serverURL) {
        this.serverURL = serverURL;
        String url = serverURL.replace("http", "ws") + "websocket";
        this.session = connect(url);
    }

    /***
     * Registers the provided consumer to receive updates
     * @param consumer the Consumer function of the specified type
     * @param destination the URL part (e.g. /topics/events/[ID]
     * @param payloadType the Class of the object being sent across the socket
     * @param <T> the Class of the object to be processed (same as of the payload)
     */
    public <T> void registerForMessages(Consumer<T> consumer, String destination,
                                        Class<T> payloadType){
        session.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return payloadType;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("This should print when a message is received, never does");
                //noinspection unchecked
                consumer.accept((T) payload);
            }
        });
    }

    private StompSession connect(String url){
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            return stomp.connectAsync(url, new StompSessionHandlerAdapter() {}).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throw new IllegalStateException();
    }
}
