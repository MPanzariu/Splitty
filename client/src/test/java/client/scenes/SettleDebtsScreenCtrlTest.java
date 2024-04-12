package client.scenes;

import client.utils.*;
import commons.Event;
import commons.Participant;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import static client.TestObservableUtils.stringToObservable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
class SettleDebtsScreenCtrlTest {
    @Mock
    MainCtrl mainCtrl;
    @Mock
    Translation translation;
    @Mock
    SettleDebtsUtils settleUtils;
    @Mock
    ImageUtils imageUtils;
    @Mock
    EmailHandler emailHandler;
    @Mock
    Styling styling;
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

    @BeforeEach
    void setup(){
        lenient().doReturn(stringToObservable("Binding!")).when(translation).getStringBinding(anyString());
        Image testImage = new WritableImage(1,1);
        lenient().doReturn(new ImageView(testImage)).when(imageUtils).generateImageView(anyString(), anyInt());
    }

    @Test
    void populateVBoxTest(){
        VBox testBox = new VBox();
        sut.populateVBox(testBox, new Event("Title!", null));
    }

    @Test
    void validTransferLabelText(){
        Transfer exampleTransfer = new Transfer(new Participant("A!"), 19216801, new Participant("B!"));
        ObservableValue<String> observableText = stringToObservable("A! gives 192168.01\u20ac to B!");
        when(settleUtils.createTransferString(exampleTransfer)).thenReturn(observableText);
        Label transferLabel = sut.generateTransferLabel(exampleTransfer);
        assertEquals(observableText.getValue(), transferLabel.textProperty().getValue());
    }
}