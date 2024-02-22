package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ParticipantTest {
    @Test
    public void checkConstructor() {
        Event e = new Event();
        var p = new Participant("t", e);
        assertEquals("t", p.getName());
        assertEquals(e, p.getEvent());
        assertTrue(p.getExpensesOwedTo().isEmpty());
    }

    @Test
    public void setterGetterNameTest(){
        Event e = new Event();
        var p = new Participant("t", e);
        assertEquals("t", p.getName());
        p.setName("n");
        assertEquals("n", p.getName());
    }

    @Test
    public void expensesTest(){
        Event e = new Event();
        var p = new Participant("t", e);
        assertTrue(p.getExpensesOwedTo().isEmpty());
        Expense ex = new Expense();
        p.addExpense(ex);
        assertTrue(p.getExpensesOwedTo().contains(ex));
        p.removeExpense(ex);
        assertTrue(p.getExpensesOwedTo().isEmpty());
    }

    @Test
    public void equalsHashCode() {
        Event e = new Event();
        var a = new Participant("t", e);
        var b = new Participant("t", e);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        Event e = new Event();
        var a = new Participant("t", e);
        var b = new Participant("t1", e);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        Event e = new Event();
        var actual = new Participant("a", e).toString();
        assertTrue(actual.contains(Event.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("event"));
        assertTrue(actual.contains("expensesOwedTo"));
        assertTrue(actual.contains("id"));
    }
}
