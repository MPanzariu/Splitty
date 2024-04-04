package client.scenes;

import client.utils.SettleDebtsUtils;
import client.utils.Transfer;
import client.utils.Translation;
import commons.Participant;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import static client.TestObservableUtils.stringToObservable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
class SettleDebtsScreenCtrlTest {
    @Mock
    MainCtrl mainCtrl;
    @Mock
    Translation translation;
    @Mock
    SettleDebtsUtils settleUtils;
    @InjectMocks
    SettleDebtsScreenCtrl sut;
    @BeforeAll
    static void testFXSetup(){
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
    }

    @Test
    void exampleTest(){
        Transfer exampleTransfer = new Transfer(new Participant("A!"), 19216801, new Participant("B!"));
        ObservableValue<String> observableText = stringToObservable("A! gives 192168.01\u20ac to B!");
        when(settleUtils.createTransferString(exampleTransfer)).thenReturn(observableText);
        Label transferLabel = sut.generateTransferLabel(exampleTransfer);
        assertEquals(observableText.getValue(), transferLabel.textProperty().getValue());
    }
}