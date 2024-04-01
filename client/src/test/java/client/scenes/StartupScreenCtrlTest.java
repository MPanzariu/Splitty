package client.scenes;

import client.utils.AppStateManager;
import client.utils.ServerUtils;
import client.utils.Translation;
import commons.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class StartupScreenCtrlTest{

    private TestStartupScreenCtrl sut;
    private TestServerUtils testServerUtils;
    private TestMainController testMainController;
    private LanguageIndicatorCtrl languageCtrl;
    private AppStateManager manager;
    @BeforeEach
    public void setup() {
        this.testServerUtils = new TestServerUtils();
        this.testMainController =  new TestMainController();
        this.languageCtrl = mock(LanguageIndicatorCtrl.class);
        this.manager = mock(AppStateManager.class);
        sut = new TestStartupScreenCtrl(this.testServerUtils, this.testMainController, null,
                languageCtrl, manager);

    }

    @Test
    public void testInitialization(){
        assertTrue(sut.labelBindings.isEmpty());
        assertTrue(sut.textBoxBindings.isEmpty());
        assertTrue(sut.buttonBindings.isEmpty());
        sut.initialize(null, null);
        assertEquals(4, sut.labelBindings.size());
        assertEquals(2, sut.textBoxBindings.size());
        assertEquals(3, sut.buttonBindings.size());
    }

    @Test
    public void testCreateEventEmptyTitle(){
        String title = "";
        sut.textBoxText = title;
        sut.createEvent();
        assertTrue(sut.joinEventCalls.isEmpty());
        assertTrue(testServerUtils.calls.isEmpty());
    }

    @Test
    public void testCreateEventSuccess(){
        String title = "title";
        //Pending JavaFX testing changes being merged!
//        sut.textBoxText = title;
//        sut.createEvent();
//        assertEquals(testServerUtils.calls.size(), 1);
//        assertEquals(sut.joinEventCalls.size(), 1);
    }
    @Test
    public void testJoinEventInvalidLength(){
        String inviteCode = "invalid";
        sut.textBoxText = inviteCode;
        sut.joinEventClicked();
        assertEquals(0, testServerUtils.calls.size());
        assertEquals(0, testMainController.calls.size());
        assertFalse(sut.labelBindings.isEmpty());
        assertTrue(sut.labelBindings.contains("Startup.Label.InvalidCode"));
    }

    @Test
    public void testJoinEventInvalidCode(){
        //The only valid code is "aaaaaa"
        String inviteCode = "aaaaab";
        sut.textBoxText = inviteCode;
        sut.joinEventClicked();
        assertEquals(1, testServerUtils.calls.size());
        assertFalse(sut.labelBindings.isEmpty());
        assertTrue(sut.labelBindings.contains("Startup.Label.InvalidCode"));
    }

    @Test
    public void testJoinEventValidCode(){
        String inviteCode = "aaaaaa";
        //Pending JavaFX testing changes being merged!
//        sut.textBoxText = inviteCode;
//        sut.joinEventClicked();
//        assertEquals(1, testServerUtils.calls.size());
//        assertFalse(sut.joinEventCalls.isEmpty());
    }

    @Test
    public void testRemoveFromVboxNull(){
        sut.removeFromVBox(null);
    }

    @Test
    public void testRemoveFromHistoryIfExists(){
        HBox hBox = null; //Will be changed with JavaFX testing changes
        sut.getEventsAndHBoxes().put("ABC123", hBox);
        assertFalse(sut.getEventsAndHBoxes().isEmpty());
        sut.removeFromHistoryIfExists("ABC123");
        //Pending JavaFX testing changes
//        assertTrue(sut.getEventsAndHBoxes().isEmpty());
    }

    private class TestServerUtils extends ServerUtils{
        public List<String> calls = new LinkedList<>();
        public String validInvitationCode = "aaaaaa";
        @Override
        public Event getEvent(String inviteCode){
            calls.add("getEvent: " + inviteCode);
            //valid code
            if(inviteCode.equals(validInvitationCode)){
                return new Event();
            }
            //invalid code
            throw new jakarta.ws.rs.BadRequestException();
        }

        @Override
        public Event createEvent(String inviteCode){
            calls.add("createEvent: " + inviteCode);
            return new Event();
        }
    }

    private class TestMainController extends MainCtrl{
        public List<String> calls = new LinkedList<>();

        public TestMainController() {
            super(null, null);
        }

        @Override
        public void switchEvents(String eventCode){
            calls.add("join");
        }
    }

    private class TestStartupScreenCtrl extends StartupScreenCtrl{
        public String textBoxText;
        public List<String> joinEventCalls = new ArrayList<>();

        public List<String> labelBindings = new ArrayList<>();
        public List<String> textBoxBindings = new ArrayList<>();
        public List<String> buttonBindings = new ArrayList<>();
        public HashMap<HBox, Event> hBoxEventHashMap;
        public HashMap<Event, HBox> eventHBoxHashMap;
        /**
         * Constructor
         *
         * @param server      the ServerUtils instance
         * @param mainCtrl    the MainCtrl instance
         * @param translation the Translation to use
         */
        public TestStartupScreenCtrl(ServerUtils server, MainCtrl mainCtrl, Translation translation,
                                     LanguageIndicatorCtrl languageCtrl, AppStateManager manager) {
            super(server, mainCtrl, translation, languageCtrl, manager);
        }

        @Override
        public String getTextBoxText(TextField textBox){
            return textBoxText;
        }

        @Override
        public void bindLabel(Label label,String str){
            labelBindings.add(str);
            return;
        }

        @Override
        public void bindTextBox(TextField textBox, String str){
            textBoxBindings.add(str);
            return;
        }

        @Override
        public void bindButton(Button button, String str){
            buttonBindings.add(str);
            return;
        }

        @Override
        public void switchToEvent(String eventId){
            joinEventCalls.add(eventId);
        }

        @Override
        public List<Node> getHistoryNodes(){
            return new ArrayList<>();
        }
    }
}
