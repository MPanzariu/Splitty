package client.scenes;

import client.utils.Translation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
public class ParticipantScreenCtrlTest {
    @Mock
    MainCtrl mainCtrl;
    @Mock
    Translation translation;
    @InjectMocks
    ParticipantScreenCtrl participantScreenCtrl;
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
