package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Event{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    @Column(unique=true)
    private String code;
    @OneToMany(mappedBy = "event", orphanRemoval = true)
    private Set<Participant> participants;
    @OneToMany(mappedBy = "event", orphanRemoval = true)
    private Set<Expense> expenses;

    @SuppressWarnings("unused")
    public Event() {}

    public Event(String title, String code) {
        this.title = title;
        this.code = code;
        this.participants = new HashSet<>();
        this.expenses = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public void addParticipant(Participant participant){
        participants.add(participant);
    }

    public void removeParticipant(Participant participant){
        participants.remove(participant);
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void addExpense(Expense expense){
        expenses.add(expense);
    }

    public void removeExpense(Expense expense){
        expenses.remove(expense);
    }

    public Set<Expense> getExpenses(){
        return expenses;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }


}