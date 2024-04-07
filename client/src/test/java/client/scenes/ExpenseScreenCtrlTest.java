package client.scenes;

import client.utils.ImageUtils;
import client.utils.ServerUtils;
import client.utils.Translation;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExpenseScreenCtrlTest {

    private ExpenseScreenCtrl expenseScreenCtrl;
    private ServerUtils serverUtilsMock;
    private MainCtrl mainCtrlMock;
    private Translation translationMock;

    @BeforeEach
    public void setUp() {
        serverUtilsMock = mock(ServerUtils.class);
        mainCtrlMock = mock(MainCtrl.class);
        translationMock = mock(Translation.class);
        expenseScreenCtrl = new ExpenseScreenCtrl(serverUtilsMock, mainCtrlMock, translationMock);
    }

    @Test
    public void testGetParticipantList_EmptyEvent() {
        // Given an empty event
        Event event = new Event();
        expenseScreenCtrl.setCurrentEvent(event);

        // When getParticipantList is called
        ObservableList<String> participantList = expenseScreenCtrl.getParticipantList();

        // Then the participantList should be empty
        assertEquals(0, participantList.size());
    }

    @Test
    public void testGetParticipantList_NonEmptyEvent() {
        // Given a non-empty event with participants
        Set<Participant> participants = new HashSet<>();
        participants.add(new Participant("John"));
        participants.add(new Participant("Alice"));
        participants.add(new Participant("Bob"));
        Event event = new Event("Event", null);
        for(Participant participant: participants)
            event.addParticipant(participant);
        expenseScreenCtrl.setCurrentEvent(event);

        // When getParticipantList is called
        ObservableList<String> participantList = expenseScreenCtrl.getParticipantList();

        // Then the participantList should contain names of all participants
        assertEquals(3, participantList.size());
        assertTrue(participantList.contains("John"));
        assertTrue(participantList.contains("Alice"));
        assertTrue(participantList.contains("Bob"));
    }
}