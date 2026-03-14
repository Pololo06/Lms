package edu.unimagdalena.lms2.repositories;

import edu.unimagdalena.lms2.entities.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
    List<Assessment> findByType(String type);

    List<Assessment> findByScoreGreaterThan(int score);

    List<Assessment> findByTakenAtBefore(Instant date);

    @Query("""
       SELECT COUNT(a)
       FROM Assessment a
       WHERE a.student.id = :studentId
       """)
    Long countAssessmentsByStudent(@Param("studentId") UUID studentId);
}
