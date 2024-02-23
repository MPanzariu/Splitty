package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ParticipantTest {
    /**
     * Test for constructor
     */
    @Test
    public void checkConstructor() {
        Event e = new Event();
        var p = new Participant("name", e);
        assertEquals("name", p.getName());
        assertEquals(e, p.getEvent());
        assertTrue(p.getExpensesOwedTo().isEmpty());
    }

    /**
     * Test for name getter and setter
     */
    @Test
    public void setterGetterNameTest(){
        Event e = new Event();
        var p = new Participant("name", e);
        assertEquals("name", p.getName());
        p.setName("new name");
        assertEquals("new name", p.getName());
    }

    /**
     * Test for adding and removing expenses
     */
    @Test
    public void expensesTest(){
        Event e = new Event();
        var p = new Participant("name", e);
        assertTrue(p.getExpensesOwedTo().isEmpty());
        Expense ex = new Expense();
        p.addExpense(ex);
        assertTrue(p.getExpensesOwedTo().contains(ex));
        p.removeExpense(ex);
        assertTrue(p.getExpensesOwedTo().isEmpty());
    }
    /**
     * Equality checker for equal expenses
     */
    @Test
    public void equalsHashCode() {
        Event e = new Event();
        var a = new Participant("name", e);
        var b = new Participant("name", e);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
    /**
     * Equality checker for unequal expenses
     */
    @Test
    public void notEqualsHashCode() {
        Event e = new Event();
        var a = new Participant("name", e);
        var b = new Participant("new name", e);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }
    /**
     * Tests for toString
     */
    @Test
    public void hasToString() {
        Event e = new Event();
        var actual = new Participant("name", e).toString();
        assertTrue(actual.contains(Event.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("event"));
        assertTrue(actual.contains("expensesOwedTo"));
        assertTrue(actual.contains("id"));
    }
}
