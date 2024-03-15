package client.utils;

import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;

public class ManagementOverviewUtils {

    private final Translation translation;
    private final ServerUtils server;
    private final ObservableList<Event> events = FXCollections.observableArrayList();
    private final SimpleStringProperty ascending = new SimpleStringProperty();
    private final SimpleStringProperty descending = new SimpleStringProperty();
    private final StringProperty order = new SimpleStringProperty();

    @Inject
    public ManagementOverviewUtils(Translation translation, ServerUtils server) {
        this.translation = translation;
        this.server = server;
    }

    /**
     * Configure the translation for all order types.
     * @return an ObservableList which contains all the order types, each bound to their own translation.
     */
    public ObservableList<StringProperty> setOrderTypes() {
        StringProperty title = new SimpleStringProperty();
        StringProperty creationDate = new SimpleStringProperty();
        StringProperty lastActivity = new SimpleStringProperty();
        title.bind(translation.getStringBinding("ManagementOverview.ComboBox.title"));
        creationDate.bind(translation.getStringBinding("ManagementOverview.ComboBox.creationDate"));
        lastActivity.bind(translation.getStringBinding("ManagementOverview.ComboBox.lastActivity"));
        return FXCollections.observableArrayList(title, creationDate, lastActivity);
    }

    /**
     * Retrieve events from the database and sorts them.
     * @return ObservableList of all events sorted by their title in ascending order.
     */
    public ObservableList<Event> retrieveEvents() {
        events.setAll(server.retrieveAllEvents());
        events.sort(Comparator.comparing(event -> event.getTitle().toLowerCase()));
        return events;
    }

    /**
     * Binds the sort button to its two states, translated by Translation.
     * @return The property bound to ascending.
     */
    public StringProperty bindSortButton() {
        ascending.bind(translation.getStringBinding("ManagementOverview.Button.ascending"));
        descending.bind(translation.getStringBinding("ManagementOverview.Button.descending"));
        order.bind(ascending);
        return order;
    }

    /**
     * Orders event by their title.
     * If the events are sorted in ascending order, then they will be sorted in descending order, and vice versa.
     * The titles are considered as lowercase strings while sorting.
     * @param text Text of the sort button
     */
    public void sortEventsByTitle(String text) {
        if(text.equals(ascending.getValue())) {
            events.sort(Comparator.comparing((Event event) -> event.getTitle().toLowerCase()).reversed());
            order.bind(descending);
        } else if(text.equals(descending.getValue())) {
            events.sort(Comparator.comparing(event -> event.getTitle().toLowerCase()));
            order.bind(ascending);
        }
    }

    /**
     * Initializes the list of participants.
     * @param event Selected event
     * @return Observable list of the selects event's participant
     */
    public ObservableList<Participant> initializeParticipantsList(Event event) {
        return FXCollections.observableArrayList(event.getParticipants());
    }

    /**
     * Initializes the list of expenses
     * @param event Selected event
     * @return Observable list of the event's expenses
     */
    public ObservableList<Expense> initializeExpenseList(Event event) {
        return FXCollections.observableArrayList(event.getExpenses());
    }

    public ObservableList<Event> getEvents() {
        return events;
    }

    public SimpleStringProperty getAscending() {
        return ascending;
    }

    public SimpleStringProperty getDescending() {
        return descending;
    }
    public void bindAscending(StringProperty ascending) {
        this.ascending.bind(ascending);
    }

    public void bindDescending(StringProperty descending) {
        this.descending.bind(descending);
    }

    public StringProperty getOrder() {
        return order;
    }
    public void bindOrder(StringProperty order) {
        this.order.bind(order);
    }

    public void setEvents(ObservableList<Event> events) {
        this.events.setAll(events);
    }
}
