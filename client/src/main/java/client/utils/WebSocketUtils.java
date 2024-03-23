package client.utils;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.application.Platform;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class WebSocketUtils {

    private final StompSession session;


    /***
     * WebSocketUtils constructor
     * @param serverURL the server URL to use (injected)
     */
    @Inject
    public WebSocketUtils(@Named("connection.URL") String serverURL) {
        String url = serverURL.replace("http", "ws") + "websocket";
        this.session = connect(url);
    }

    /***
     * Registers the provided consumer to receive updates
     * @param consumer the Consumer function of the specified type
     * @param destination the URL part (e.g. /topics/events/[ID]
     * @param payloadType the Class of the object being sent across the socket
     * @param <T> the Class of the object to be processed (same as of the payload)
     * @return the Subscription generated, which can be used to unsubscribe
     */
    public <T> StompSession.Subscription registerForMessages(Consumer<T> consumer,
                                                             String destination,
                                                             Class<T> payloadType){
        return session.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return payloadType;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                //noinspection unchecked
                Platform.runLater(() -> consumer.accept((T) payload));
            }
        });
    }

    /***
     * Establishes the STOMP WebSocket
     * @param url the server URL to use
     * @return a StompSession for use in further communication
     */
    private StompSession connect(String url){
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try{
            return stomp.connectAsync(url, new StompSessionHandlerAdapter() {
                @Override
                public void handleException(StompSession session,
                                            StompCommand command,
                                            StompHeaders headers,
                                            byte[] payload,
                                            Throwable exception) {
                    System.out.println("WEBSOCKET EXCEPTION:");
                    exception.printStackTrace();
                    super.handleException(session, command, headers, payload, exception);
                }

                @Override
                public void handleTransportError(StompSession session,
                                                 Throwable exception) {
                    System.out.println("WEBSOCKET TRANSPORT ERROR:");
                    exception.printStackTrace();
                    super.handleTransportError(session, exception);
                }
            }).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throw new IllegalStateException();
    }
}
