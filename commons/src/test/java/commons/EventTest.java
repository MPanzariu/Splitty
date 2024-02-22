package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventTest {
    @Test
    public void checkConstructor() {
        var e = new Event("t", "c");
        assertEquals("t", e.getTitle());
        assertEquals("c", e.getCode());
    }

    @Test
    public void getterSetterTitleCheck(){
        var e = new Event("title", "c");
        assertEquals("title",e.getTitle());
        e.setTitle("t1");
        assertEquals("t1",e.getTitle());
    }

    @Test
    public void participantsCheck(){
        var e = new Event("title", "c");
        assertTrue(e.getParticipants().isEmpty());
        Participant p = new Participant();
        e.addParticipant(p);
        assertSame(1, e.getParticipants().size());
        e.removeParticipant(p);
        assertTrue(e.getParticipants().isEmpty());
    }

    @Test
    public void expensesCheck(){
        var e = new Event("title", "c");
        assertTrue(e.getExpenses().isEmpty());
        Expense ex = new Expense();
        e.addExpense(ex);
        assertSame(1, e.getExpenses().size());
        e.removeExpense(ex);
        assertTrue(e.getExpenses().isEmpty());
    }

    @Test
    public void equalsHashCode() {
        var a = new Event("t", "c");
        var b = new Event("t", "c");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new Event("t", "c");
        var b = new Event("g", "c");
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var actual = new Event("a", "b").toString();
        assertTrue(actual.contains(Event.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("code"));
        assertTrue(actual.contains("expenses"));
        assertTrue(actual.contains("id"));
        assertTrue(actual.contains("participants"));
        assertTrue(actual.contains("title"));
    }


}
