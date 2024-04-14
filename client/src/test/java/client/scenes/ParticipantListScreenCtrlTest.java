package client.scenes;

import client.utils.*;
import commons.Event;
import commons.Participant;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
class ParticipantListScreenCtrlTest {
    @Mock
    MainCtrl mainCtrl;
    @Mock
    Translation translation;
    @Mock
    ServerUtils server;
    @Mock
    ImageUtils imageUtils;
    @Mock
    Styling styling;
    @InjectMocks
    ParticipantListScreenCtrl sut;

    private Event event;
    private Participant participant1;
    private Participant participant2;

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
        event = new Event("Title!", null);
        participant1 = new Participant("Alastor");
        participant1.setEmail("");
        participant2 = new Participant("Vox");
        participant2.setLegalName("Vox V.");
        participant2.setIban("VOXTEK534645645");
        participant2.setBic("HELLBANK666");
        participant2.setEmail("vox@voxtek.com");
        event.addParticipant(participant1);
        event.addParticipant(participant2);
    }

    @Test
    void participantListGenerationTest(){
        ListView<HBox> participantList = new ListView<>();
        Image testImage = new WritableImage(1,1);
        doReturn(testImage).when(imageUtils).loadImageFile("x_remove.png");
        doReturn(new ImageView(testImage)).when(imageUtils).generateImageView(any(Image.class), eq(15));

        sut.refreshParticipantList(participantList, event);
        ObservableList<HBox> hBoxes = participantList.getItems();

        List<String> labelTexts = new LinkedList<>();
        for(HBox box: hBoxes){
            var children = box.getChildren();
            HBox innerBox = null;
            for(Node child: children){
                try{
                    innerBox = (HBox) child;
                } catch (ClassCastException ignored) {}
            }
            assertNotNull(innerBox);
            Label innerInnerLabel = (Label) innerBox.getChildren().get(0);
            String labelText = innerInnerLabel.getText();
            labelTexts.add(labelText);
        }

        assertTrue(labelTexts.contains(participant1.getName()));
        assertTrue(labelTexts.contains(participant2.getName()));
    }

}