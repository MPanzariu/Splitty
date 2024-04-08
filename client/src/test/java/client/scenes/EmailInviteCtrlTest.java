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
    private MainCtrl mainCtrl;

    @BeforeEach
    void init(){
        translation = mock(Translation.class);
        emailHandler = mock(EmailHandler.class);
        serverUtils = mock(ServerUtils.class);
        mainCtrl = mock(MainCtrl.class);
        emailInviteCtrl = new EmailInviteCtrl(translation,serverUtils,mainCtrl,emailHandler);

    }

    @Test
    void cancelButton(){
        emailInviteCtrl.cancel();
        verify(mainCtrl).switchScreens(EventScreenCtrl.class);
    }

    @Test
    void checkEmail() {
        assertTrue(emailInviteCtrl.checkEmail("test@test.com"));
        assertFalse(emailInviteCtrl.checkEmail("@test.com"));
        assertFalse(emailInviteCtrl.checkEmail("test@test"));
    }
}

