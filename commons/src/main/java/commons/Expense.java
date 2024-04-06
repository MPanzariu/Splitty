package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

import java.util.HashSet;

import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Expense.class)
public class Expense{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private int priceInCents;
    private Date date;
    private String currency;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE})
    private Tag expenseTag;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Participant owedTo;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Participant> participantsInExpense;

    @SuppressWarnings("unused")
    public Expense() {}

    public Expense(String name, int priceInCents, Date date, Participant owedTo) {
        this.name = name;
        this.priceInCents = priceInCents;
        this.date = date;
        this.owedTo = owedTo;
        this.participantsInExpense = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPriceInCents(int priceInCents) {
        this.priceInCents = priceInCents;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setOwedTo(Participant participant){
        this.owedTo = participant;
    }

    public Participant getOwedTo(){
        return owedTo;
    }

    public Tag getExpenseTag() {
        return expenseTag;
    }
    public void setExpenseTag(Tag expenseTag) {
        this.expenseTag = expenseTag;
    }

    public Set<Participant> getParticipantsInExpense() {
        return participantsInExpense;
    }
    public void addParticipantToExpense(Participant participant) {
        participantsInExpense.add(participant);
    }
    public void removeParticipantFromExpense(Participant participant) {
        this.participantsInExpense.remove(participant);
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

    public String stringOnScreen() {
        double price = priceInCents / 100.;
        if(price == (int)price)
            return owedTo.getName() + " paid " + (int) price + (char)128 + " for " + name;
        return owedTo.getName() + " paid " + price + (char)128 + " for " + name;

    }

    public void setParticipantToExpense(Set<Participant> participants) {
        participantsInExpense = participants;
    }
}
