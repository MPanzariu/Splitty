package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import javafx.scene.control.PasswordField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ManagementOverviewPasswordCtrlTest {
    private Translation translation;
    private ServerUtils server;
    private TestManagementOverviewPasswordCtrl sut;
    private MainCtrl mainCtrl;
    @BeforeEach
    public void setUp(){
        translation = mock(Translation.class);
        server = mock(ServerUtils.class);
        mainCtrl = mock(MainCtrl.class);
        sut = new TestManagementOverviewPasswordCtrl(server, mainCtrl, translation);
    }
    @Test
    void logInCheckValid() {
        sut.textBoxText = "password";
        when(server.checkPassword("password")).thenReturn(true);
        sut.logInCheck(null);
        //should only contain a binding to empty
        assertEquals(1, mockingDetails(translation).getInvocations().size());
        assertTrue(sut.textBoxText.isEmpty());
    }

    @Test
    void logInCheckNull() {
        sut.textBoxText = null;
        when(server.checkPassword(anyString())).thenReturn(true);
        sut.logInCheck(null);
        assertEquals(1, mockingDetails(translation).getInvocations().size());
        assertTrue(sut.textBoxText.isEmpty());
    }

    @Test
    void logInCheckInvalidPassword() {
        sut.textBoxText = "invalid";
        when(server.checkPassword("invalid")).thenReturn(false);
        sut.logInCheck(null);
        assertTrue(sut.textBoxText.isEmpty());
    }

    @Test
    void logInCheckEmptyPassword() {
        sut.textBoxText = "";
        when(server.checkPassword(anyString())).thenReturn(true);
        assertEquals(0, mockingDetails(translation).getInvocations().size());
        sut.logInCheck(null);
        assertEquals(1, mockingDetails(translation).getInvocations().size());
        assertTrue(sut.textBoxText.isEmpty());
    }

    @Test
    void goBackToMainScreen() {
        sut.goBackToMain(null);
        assertEquals(1, mockingDetails(mainCtrl).getInvocations().size());
    }

    @Test
    void intializeTest(){
        sut.initialize(null, null);
        assertEquals(3, mockingDetails(translation).getInvocations().size());
    }


    private class TestManagementOverviewPasswordCtrl extends ManagementOverviewPasswordCtrl {
        public String textBoxText;
        public TestManagementOverviewPasswordCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation) {
            super(server, mainCtrl, translation);
        }

        @Override
        public String getPasswordFieldText(PasswordField passwordField){
            return textBoxText;
        }

        @Override
        public void clearPasswordField(){
            textBoxText = "";
        }
    }
}