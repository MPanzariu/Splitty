package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
public class StartupScreenCtrlTest{

    private StartupScreenCtrl sut;
    private TestServerUtils testServerUtils;
    private TestMainController testMainController;
    @Mock
    private TextField eventTitleTextBox;

    @Mock
    private TextField inviteCodeTextBox;

    @Mock
    private Label createEventFeedback;

    @Mock
    private Label joinEventFeedback;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.testServerUtils = new TestServerUtils();
        this.testMainController =  new TestMainController();
        sut = new StartupScreenCtrl(this.testServerUtils, this.testMainController);
        eventTitleTextBox = new TextField();
        inviteCodeTextBox = new TextField();
        sut.setEventTitleTextBox(eventTitleTextBox);
        sut.setInviteCodeTextBox(inviteCodeTextBox);
        sut.setCreateEventFeedback(createEventFeedback);
        sut.setJoinEventFeedback(joinEventFeedback);
    }

    @Test
    public void testCreateEventEmptyTitle(){
        String title = "";
//        eventTitleTextBox.setText(title);
//        sut.createEvent();
//        assertTrue(testServerUtils.calls.isEmpty());
//        assertTrue(testMainController.calls.isEmpty());
    }
//
//    @Test
//    public void testCreateEventSuccess(){
//        String title = "title";
//        eventTitleTextBox.setText(title);
//        sut.createEvent();
//        assertEquals(1, testServerUtils.calls.size());
//        assertEquals(1, testMainController.calls.size());
//    }
//
//    @Test
//    public void testJoinEventInvalidLength(){
//        String inviteCode = "invalid";
//        inviteCodeTextBox.setText(inviteCode);
//        sut.joinEvent();
//        assertEquals(0, testServerUtils.calls.size());
//        assertEquals(0, testMainController.calls.size());
//    }
//
//    @Test
//    public void testJoinEventInvalidCode(){
//        String inviteCode = "aaaaab";
//        inviteCodeTextBox.setText(inviteCode);
//        sut.joinEvent();
//        assertEquals(1, testServerUtils.calls.size());
//        assertEquals(0, testMainController.calls.size());
//    }
//
//    @Test
//    public void testJoinEventValidCode(){
//        String inviteCode = "aaaaaa";
//        inviteCodeTextBox.setText(inviteCode);
//        sut.joinEvent();
//        assertEquals(1, testServerUtils.calls.size());
//        assertEquals(1, testMainController.calls.size());
//    }



    public class TestServerUtils extends ServerUtils{
        public List<String> calls = new LinkedList<>();
        @Override
        public Event getEvent(String inviteCode){
            calls.add("getEvent: " + inviteCode);
            //valid code
            if(inviteCode.equals("aaaaaa")){
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

    public class TestMainController extends MainCtrl{
        public List<String> calls = new LinkedList<>();
        public Event lastEvent;
        @Override
        public void joinEvent(Event event){
            lastEvent = event;
            calls.add("join");
        }
    }
}
