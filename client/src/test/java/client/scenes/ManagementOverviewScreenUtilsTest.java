package client.scenes;

import client.utils.ManagementOverviewUtils;
import client.utils.ServerUtils;
import client.utils.Translation;
import commons.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ManagementOverviewScreenUtilsTest {

    Translation translation;
    ServerUtils server;
    ManagementOverviewUtils utils;

    @BeforeEach
    public void setup() {
        translation = mock(Translation.class);
        server = mock(ServerUtils.class);
        utils = new ManagementOverviewUtils(translation, server);
    }

    /**
     * Tests whether the elements of the return value are bound to the values injected by Translation.
     */
    @Test
    public void setOrderTypes() {
        StringProperty title = new SimpleStringProperty("Title");
        StringProperty creationDate = new SimpleStringProperty("Creation Date");
        StringProperty lastActivity = new SimpleStringProperty("Last Activity");
        when(translation.getStringBinding("ManagementOverview.ComboBox.title"))
                .thenReturn(title);
        when(translation.getStringBinding("ManagementOverview.ComboBox.creationDate"))
                .thenReturn(creationDate);
        when(translation.getStringBinding("ManagementOverview.ComboBox.lastActivity"))
                .thenReturn(lastActivity);
        ObservableList<StringProperty> actual = utils.setOrderTypes();
        assertEquals("Title", actual.get(0).getValue());
        assertEquals("Creation Date", actual.get(1).getValue());
        assertEquals("Last Activity", actual.get(2).getValue());
        assertTrue(actual.get(0).isBound());
        assertTrue(actual.get(1).isBound());
        assertTrue(actual.get(2).isBound());
    }

    /**
     * Events from ServerUtils should be retrieved, and sorted correctly.
     */
    @Test
    public void retrieveEvents() {
        Event event1 = new Event("Party", null);
        Event event2 = new Event("Holiday", null);
        Event event3 = new Event("party", null);
        when(server.retrieveAllEvents()).thenReturn(List.of(event1, event2, event3));
        ObservableList<Event> expected = FXCollections.observableArrayList(event2, event1, event3);
        ObservableList<Event> actual = utils.retrieveEvents();
        assertEquals(expected, actual);
        assertEquals(expected, utils.getEvents());
    }

    /**
     * Ascending and descending fields should be bound correctly by Translation.
     * The initial state of order type should be ascending.
     */
    @Test
    public void bindSortButton() {
        when(translation.getStringBinding("ManagementOverview.Button.ascending"))
                .thenReturn(new SimpleStringProperty("Ascending"));
        when(translation.getStringBinding("ManagementOverview.Button.descending"))
                .thenReturn(new SimpleStringProperty("Descending"));
        StringProperty order = utils.bindSortButton();
        assertEquals("Descending", utils.getDescending().getValue());
        assertEquals("Ascending", utils.getAscending().getValue());
        assertEquals(utils.getAscending().getValue(), order.getValue());
        assertTrue(order.isBound());
        assertEquals(utils.getOrder(), order);
    }

    /**
     * When button is set at ascending order, then the event list should be ordered in descending order.
     * Make sure that the order field is bound to the new state.
     */
    @Test
    public void sortEventsByTitleInDescendingOrder() {
        Event e1 = new Event("Party", null);
        Event e2 = new Event("Holiday", null);
        Event e3 = new Event("party", null);
        utils.setEvents(FXCollections.observableArrayList(e1, e2, e3));
        utils.bindAscending(new SimpleStringProperty("Ascending"));
        utils.bindDescending(new SimpleStringProperty("Descending"));
        utils.sortEventsByTitle("Ascending");
        ObservableList<Event> actual = utils.getEvents();
        ObservableList<Event> expected = FXCollections.observableArrayList(e1, e3, e2);
        assertEquals(expected, actual);
        assertEquals("Descending", utils.getOrder().getValue());
        assertTrue(utils.getOrder().isBound());
    }

    /**
     * When the button is at descending order, then the event list should be ordered in ascending order.
     * Make sure that the order field is bound to the new state.
     */
    @Test
    public void sortEventsByTitleInAscendingOrder() {
        Event e1 = new Event("Party", null);
        Event e2 = new Event("Holiday", null);
        Event e3 = new Event("party", null);
        utils.setEvents(FXCollections.observableArrayList(e2, e1, e3));
        utils.bindAscending(new SimpleStringProperty("Ascending"));
        utils.bindDescending(new SimpleStringProperty("Descending"));
        utils.sortEventsByTitle("Descending");
        ObservableList<Event> actual = utils.getEvents();
        ObservableList<Event> expected = FXCollections.observableArrayList(e2, e1, e3);
        assertEquals(expected, actual);
        assertEquals("Ascending", utils.getOrder().getValue());
        assertTrue(utils.getOrder().isBound());
    }

    /**
     * Testing getter and setters for events field.
     */
    @Test
    public void getAndSetEvents() {
        Event e = new Event("Party", null);
        ObservableList<Event> expected = FXCollections.observableArrayList(e);
        utils.setEvents(expected);
        assertEquals(expected, utils.getEvents());
    }

    /**
     * Testing getter and binder for ascending field.
     */
    @Test
    public void getAndBindAscending() {
        SimpleStringProperty ascending = new SimpleStringProperty("New");
        utils.bindAscending(ascending);
        assertEquals(ascending.getValue(), utils.getAscending().getValue());
        assertTrue(utils.getAscending().isBound());
    }

    /**
     * Testing getting and binding the descending field.
     */
    @Test
    public void getAndBindDescending() {
        SimpleStringProperty descending = new SimpleStringProperty("New");
        utils.bindDescending(descending);
        assertEquals(descending.getValue(), utils.getDescending().getValue());
        assertTrue(utils.getDescending().isBound());
    }

    /**
     * Testing getting and binding the order field.
     */
    @Test
    public void getAndBindOrder() {
        SimpleStringProperty order = new SimpleStringProperty("New");
        utils.bindOrder(order);
        assertEquals("New", utils.getOrder().getValue());
        assertTrue(utils.getOrder().isBound());
    }
}
