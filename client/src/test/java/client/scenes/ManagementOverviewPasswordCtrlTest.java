package client.scenes;

import client.utils.ServerUtils;
import client.utils.Translation;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        assertTrue(sut.bindings.isEmpty());
        sut.logInCheck(null);
        assertTrue(sut.bindings.isEmpty());
        assertTrue(sut.textBoxText.isEmpty());
    }

    @Test
    void logInCheckNull() {
        sut.textBoxText = null;
        when(server.checkPassword(anyString())).thenReturn(true);
        assertTrue(sut.bindings.isEmpty());
        sut.logInCheck(null);
        assertTrue(sut.bindings.contains("MOPCtrl.Log.In.Feedback"));
        assertTrue(sut.textBoxText.isEmpty());
    }

    @Test
    void logInCheckInvalidPassword() {
        sut.textBoxText = "invalid";
        when(server.checkPassword("invalid")).thenReturn(false);
        assertTrue(sut.bindings.isEmpty());
        sut.logInCheck(null);
        assertTrue(sut.bindings.contains("MOPCtrl.Log.In.Feedback"));
        assertTrue(sut.textBoxText.isEmpty());
    }

    @Test
    void logInCheckEmptyPassword() {
        sut.textBoxText = "";
        when(server.checkPassword(anyString())).thenReturn(true);
        assertTrue(sut.bindings.isEmpty());
        sut.logInCheck(null);
        assertTrue(sut.bindings.contains("MOPCtrl.Log.In.Feedback"));
        assertTrue(sut.textBoxText.isEmpty());
    }

    private class TestManagementOverviewPasswordCtrl extends ManagementOverviewPasswordCtrl {
        public String textBoxText;
        public ArrayList<String> bindings;
        public TestManagementOverviewPasswordCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation) {
            super(server, mainCtrl, translation);
            bindings = new ArrayList<>();
        }

        @Override
        public String getPasswordFieldText(PasswordField passwordField){
            return textBoxText;
        }

        @Override
        public void bindLabel(Label label, String str){
            bindings.add(str);
        }

        @Override
        public void clearPasswordField(){
            textBoxText = "";
        }
    }
}