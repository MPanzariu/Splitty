package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = Participant.class)
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE,
                          CascadeType.PERSIST, CascadeType.REFRESH},
                          mappedBy = "owedTo", orphanRemoval = true)
    private Set<Expense> expensesOwedTo;

    /***
     * Constructor used by Object Mappers
     */
    @SuppressWarnings("unused")
    public Participant() {}

    /***
     * Default constructor with only a name parameter
     * @param name the name of the Participant
     */
    public Participant(String name) {
        this.name = name;
        this.expensesOwedTo = new HashSet<>();
    }

    /**
     * Constructor used for testing where IDs need to be set.
     * @param id ID of the participant
     * @param name Name of the participant
     */
    public Participant(long id, String name) {
        this.id = id;
        this.name = name;
        this.expensesOwedTo = new HashSet<>();
    }

    /***
     * Specifies whether the Participant's bank account details have been filled in
     * @return a boolean confirming that all bank details are available
     */
    public boolean hasBankAccount(){
        return true;
    }

    /***
     * Provides the ID of the Participant
     * @return the Participant's ID
     */
    public long getId() {
        return id;
    }

    /***
     * Changes the Participant's name
     * @param name the new name given
     */
    public void setName(String name) {
        this.name = name;
    }

    /***
     * Provides the name of the Participant
     * @return the Participant's Name
     */
    public String getName() {
        return name;
    }

    /***
     * Adds an Expense owed to the Participant
     * @param expense the Expense owed to the Participant
     */
    public void addExpense(Expense expense){
        expensesOwedTo.add(expense);
    }

    /***
     * Removes an Expense owed to the Participant
     * @param expense the Expense to be removed from the Participant
     */
    public void removeExpense(Expense expense){
        expensesOwedTo.remove(expense);
    }

    /***
     * Provides the Set of all Expenses Owed to the Participant
     * @return the Set of all Expenses Owed to the Participant
     */
    public Set<Expense> getExpensesOwedTo() {
        return expensesOwedTo;
    }

    /***
     * Equals method for any Object
     * @param obj the Object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /***
     * Generates a HashCode for the Participant
     * @return a HashCode for the Participant
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /***
     * Generates a String representation for the Participant
     * @return a String representation for the Participant
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
