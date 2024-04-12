package client.scenes;

import client.utils.*;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.ArrayList;
import java.util.List;

import static client.TestObservableUtils.stringToObservable;
import static javafx.geometry.Pos.TOP_CENTER;
import static javafx.geometry.Pos.TOP_LEFT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    Event event;
    Participant participant1;
    Participant participant2;
    Expense expense1;
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
        participant1.setEmail("irefusetousetechnologymadeafter1933@null");
        participant2 = new Participant("Vox");
        participant2.setLegalName("Vox V.");
        participant2.setIban("VOXTEK534645645");
        participant2.setBic("HELLBANK666");
        participant2.setEmail("vox@voxtek.com");
        expense1 = new Expense("Cameras!", 666, null, participant2);
        expense1.addParticipantToExpense(participant1);
        expense1.addParticipantToExpense(participant2);
    }

    @Test
    void populateVBoxTestOneTransfer(){
        Image testImage = new WritableImage(1,1);
        ImageView testImageView = new ImageView(testImage);
        doReturn(testImage).when(imageUtils).loadImageFile("singlearrow.png");
        doReturn(testImageView).when(imageUtils).generateImageView(testImage, 25);

        String testDescriptionString = participant2.getName() + " " + participant2.getLegalName()
                + " " + participant2.getIban() + " " + participant2.getBic();
        ObservableValue<String> bankDetails = stringToObservable(testDescriptionString);
        doReturn(bankDetails).when(settleUtils).getBankDetails(participant2);

        Transfer transferGenerated = new Transfer(participant1, 333, participant2);
        String testTransferString = "Alastor gives 6.66\u20ac to Vox";
        ObservableValue<String> transferDetails = stringToObservable(testTransferString);
        doReturn(transferDetails).when(settleUtils).createTransferString(transferGenerated);

        ObservableValue<String> received = stringToObservable("Mark Received");
        doReturn(received).when(translation).getStringBinding("SettleDebts.Button.received");

        ObservableValue<String> emailInstructions = stringToObservable("Send Email");
        doReturn(emailInstructions).when(translation).getStringBinding("SettleDebts.Button.sendEmailInstructions");

        List<Transfer> transfers = List.of(transferGenerated);
        doReturn(transfers).when(settleUtils).calculateTransferInstructions(any());

        VBox testBox = new VBox();
        sut.populateVBox(testBox, event);
        ObservableList<Node> children = testBox.getChildren();
        assertEquals(2, children.size());
        assertEquals(TOP_LEFT, testBox.getAlignment());
        HBox transferBox = (HBox) children.getFirst();
        VBox detailsPane = (VBox) children.getLast();

        Label transferLabel = (Label) transferBox.getChildren().get(2);
        StringProperty transferTextResult = transferLabel.textProperty();
        assertEquals(transferDetails.getValue(), transferTextResult.getValue());

        TextArea bankDetailsArea = (TextArea) detailsPane.getChildren().getFirst();
        var bankDetailsResult = bankDetailsArea.textProperty();
        assertEquals(bankDetails.getValue(), bankDetailsResult.getValue());
    }

    @Test
    void populateVBoxTestNoTransfers() {
        VBox textBox = new VBox();
        doReturn(new ArrayList<Transfer>()).when(settleUtils).calculateTransferInstructions(any());
        ObservableValue<String> noTransfers = stringToObservable("No Transfers!");
        doReturn(noTransfers).when(translation).getStringBinding("SettleDebts.Label.noTransfers");
        sut.populateVBox(textBox, event);
        verify(translation).getStringBinding("SettleDebts.Label.noTransfers");
        assertEquals(1, textBox.getChildren().size());
        assertEquals(TOP_CENTER, textBox.getAlignment());
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