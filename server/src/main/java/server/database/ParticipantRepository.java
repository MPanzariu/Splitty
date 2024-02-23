package server.database;

import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query("SELECT p FROM Participant p WHERE p.event.code = :eventCode AND p.name = :name")
    Optional<Participant> findByEventCodeAndName(@Param("eventCode") String eventCode, @Param("name") String name);
}