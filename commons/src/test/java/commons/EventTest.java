package commons;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventTest {
    /**
     * Constructor test
     */
    @Test
    public void checkConstructor() {
        var e = new Event("title");
        assertEquals("title", e.getTitle());
    }
    /**
     * Test for getting and setting title
     */
    @Test
    public void getterSetterTitleCheck(){
        var e = new Event("title");
        assertEquals("title",e.getTitle());
        e.setTitle("new title");
        assertEquals("new title",e.getTitle());
    }
    /**
     * Test for adding and removing participants
     */
    @Test
    public void participantsCheck(){
        var e = new Event("title");
        assertTrue(e.getParticipants().isEmpty());
        Participant p = new Participant();
        e.addParticipant(p);
        assertSame(1, e.getParticipants().size());
        e.removeParticipant(p);
        assertTrue(e.getParticipants().isEmpty());
    }
    /**
     * Test for adding and removing expenses
     */
    @Test
    public void expensesCheck(){
        var e = new Event("title");
        assertTrue(e.getExpenses().isEmpty());
        Expense ex = new Expense();
        e.addExpense(ex);
        assertSame(1, e.getExpenses().size());
        e.removeExpense(ex);
        assertTrue(e.getExpenses().isEmpty());
    }
    /**
     * Equality checker for equal events
     */
    @Test
    public void equalsHashCode() {
        var a = new Event("title");
        var b = a;
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
    /**
     * Equality checker for unequal events
     */
    @Test
    public void notEqualsHashCode() {
        var a = new Event("title");
        var b = new Event("title2");
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }
    /**
     * Test for toString
     */
    @Test
    public void hasToString() {
        var actual = new Event("title").toString();
        assertTrue(actual.contains(Event.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("expenses"));
        assertTrue(actual.contains("id"));
        assertTrue(actual.contains("participants"));
        assertTrue(actual.contains("title"));
    }

    /***
     * Test to make sure IDs are not null, and generated to the right length
     */
    @Test
    public void idFormatTest(){
        Event event = new Event("title");
        String id = event.getId();
        assertEquals(Event.codeLength, id.length());
    }

}
