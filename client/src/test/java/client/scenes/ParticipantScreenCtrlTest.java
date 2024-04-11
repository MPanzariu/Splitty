package client.scenes;

import client.utils.ImageUtils;
import client.utils.Translation;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static client.TestObservableUtils.stringToObservable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class ParticipantScreenCtrlTest {
    private Participant participant1;
    private Participant participant2;
    private Event event;

    @Mock
    MainCtrl mainCtrl;
    @Mock
    Translation translation;
    @InjectMocks
    ParticipantScreenCtrl participantScreenCtrl;
    @Mock
    ImageUtils imageUtils;
    @BeforeEach
    public void setup(){
        participant1 = new Participant(1, "John");
        participant2 = new Participant(2, "Jane");
        event = new Event("Title", null);
        event.addParticipant(participant1);
        event.addParticipant(participant2);
        lenient().doReturn(stringToObservable("Binding!")).when(translation).getStringBinding(anyString());
        Image testImage = new WritableImage(1,1);
        lenient().doReturn(new ImageView(testImage)).when(imageUtils).generateImageView(anyString(), anyInt());
    }
    @BeforeAll
    static void testFXSetup(){
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
    }
    @Test
    public void testIbanValid() {
        String iban = "NL99 9999 9999 9999 9999";
        boolean result = participantScreenCtrl.checkIban(iban);
        assertTrue(result);
    }
    @Test
    public void testIbanNotValid() {
        String iban = "AA";
        boolean result = participantScreenCtrl.checkIban(iban);
        assertFalse(result);
    }
    @Test
    public void testBicValid() {
        String bic = "AAAAAAAA";
        boolean result = participantScreenCtrl.checkBic(bic);
        assertTrue(result);
    }
    @Test
    public void testBicNotValid() {
        String bic = "AA";
        boolean result = participantScreenCtrl.checkBic(bic);
        assertFalse(result);
    }
    @Test
    public void testEmailValid() {
        String email = "te@st.com";
        boolean result = participantScreenCtrl.checkEmail(email);
        assertTrue(result);
    }
    @Test
    public void testEmailNotValid() {
        String email = "test";
        boolean result = participantScreenCtrl.checkEmail(email);
        assertFalse(result);
    }

}
