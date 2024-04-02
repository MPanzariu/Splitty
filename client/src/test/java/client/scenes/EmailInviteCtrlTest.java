package client.scenes;

import client.utils.ConfigUtils;
import client.utils.EmailHandler;
import client.utils.ServerUtils;
import client.utils.Translation;
import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailInviteCtrlTest {
    private EmailInviteCtrl emailInviteCtrl;
    private Translation translation;
    private ServerUtils serverUtils;
    private EmailHandler emailHandler;
    private ConfigUtils configUtils;
    private MainCtrl mainCtrl;

    @BeforeEach
    void init(){
        translation = mock(Translation.class);
        emailHandler = mock(EmailHandler.class);
        serverUtils = mock(ServerUtils.class);
        configUtils = mock(ConfigUtils.class);
        mainCtrl = mock(MainCtrl.class);
        emailInviteCtrl = new EmailInviteCtrl(translation,serverUtils,mainCtrl,emailHandler,configUtils);

    }
    @Test
    void getInviteText() {
        Event testEvent = new Event("Test Event", null);
        emailInviteCtrl.refresh(testEvent);
        Properties properties = mock(Properties.class);
        when(this.configUtils.easyLoadProperties()).thenReturn(properties);
        when(properties.getProperty("connection.URL")).thenReturn("localhost:8080");
        System.out.println(emailInviteCtrl.getInviteText());
        String expected = String.format("You have been invited to event %s" +
                " with the invitation code of %s!" +
                " The event is hosted on the server with address: localhost:8080", testEvent.getTitle(), testEvent.getId());
        assertEquals(expected, emailInviteCtrl.getInviteText());
    }

    @Test
    void cancelButton(){
        emailInviteCtrl.cancel();
        verify(mainCtrl).switchToEventScreen();
    }
}

