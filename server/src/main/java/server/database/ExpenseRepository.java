package server.database;

import commons.Expense;
import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Set<Expense> findByOwedTo(Participant participant);

}