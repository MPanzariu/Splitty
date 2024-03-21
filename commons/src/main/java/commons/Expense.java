package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@Entity
public class Expense{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private int priceInCents;
    private Date date;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnoreProperties(value = {"expensesOwedTo"})
    private Participant owedTo;

    @SuppressWarnings("unused")
    public Expense() {}

    public Expense(String name, int priceInCents, Date date, Participant owedTo) {
        this.name = name;
        this.priceInCents = priceInCents;
        this.date = date;
        this.owedTo = owedTo;
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

    public void setOwedTo(Participant participant){
        this.owedTo = participant;
    }

    public Participant getOwedTo(){
        return owedTo;
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
        if(owedTo != null)
            return owedTo.getName() + " paid " + (double) priceInCents / 100 + " for " + name;
        else
            return null + " paid " + (double) priceInCents / 100 + " for " + name;
    }

}
