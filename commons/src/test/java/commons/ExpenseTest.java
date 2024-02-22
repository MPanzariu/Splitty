package commons;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseTest {
    @Test
    public void checkConstructor() {
        Date d = new Date();
        Event event = new Event();
        Participant p = new Participant();
        var e = new Expense("name", 10, d, event, p);
        assertEquals("name", e.getName());
        assertEquals(10, e.getPriceInCents());
        assertSame(d, e.getDate());
        assertSame(event, e.getEvent());
        assertSame(p, e.getOwedTo());
    }

    @Test
    public void setterGetterNameCheck(){
        Date d = new Date();
        Event event = new Event();
        Participant p = new Participant();
        var e = new Expense("name", 10, d, event, p);
        assertEquals("name", e.getName());
        e.setName("n");
        assertEquals("n", e.getName());
    }

    @Test
    public void setterGetterPriceCheck(){
        Date d = new Date();
        Event event = new Event();
        Participant p = new Participant();
        var e = new Expense("name", 10, d, event, p);
        assertEquals(10, e.getPriceInCents());
        e.setPriceInCents(1);
        assertEquals(1, e.getPriceInCents());
    }

    @Test
    public void setterGetterDateCheck(){
        Date d = new Date();
        Event event = new Event();
        Participant p = new Participant();
        var e = new Expense("name", 10, d, event, p);
        assertEquals(d, e.getDate());
        Date newDate = new Date();
        e.setDate(newDate);
        assertSame(newDate, e.getDate());
    }
    @Test
    public void setterGetterOwedToCheck(){
        Date d = new Date();
        Event event = new Event();
        Participant p = new Participant();
        Participant p1 = new Participant();
        var e = new Expense("name", 10, d, event, p);
        assertEquals(p, e.getOwedTo());
        e.setOwedTo(p1);
        assertSame(p1, e.getOwedTo());
    }

    @Test
    public void equalsHashCode() {
        Date d = new Date();
        Event event = new Event();
        Participant p = new Participant();
        var a = new Expense("name", 10, d, event, p);
        var b = new Expense("name", 10, d, event, p);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        Date d = new Date();
        Event event = new Event();
        Participant p = new Participant();
        var a = new Expense("name", 10, d, event, p);
        var b = new Expense("name1", 10, d, event, p);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        Date d = new Date();
        Event event = new Event();
        Participant p = new Participant();
        var actual = new Expense("name", 10, d, event, p).toString();
        assertTrue(actual.contains(Event.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("name"));
        assertTrue(actual.contains("priceInCents"));
        assertTrue(actual.contains("id"));
        assertTrue(actual.contains("date"));
        assertTrue(actual.contains("event"));
        assertTrue(actual.contains("participants"));
    }

}
