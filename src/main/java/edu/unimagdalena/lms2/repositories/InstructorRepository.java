package edu.unimagdalena.lms2.repositories;

import edu.unimagdalena.lms2.entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor, UUID> {

    Optional<Instructor> findByFullNameIgnoreCase(String fullName);

    Optional<Instructor> findByEmail(String email);

    List<Instructor> findByCreatedAtAfter (Instant date);

    List<Instructor> findTop10ByOrderByUpdatedAtAsc();

    @Query("""
       SELECT i
       FROM Instructor i
       LEFT JOIN FETCH i.instructorProfile
       """)
    List<Instructor> findInstructorsWithProfile();

    @Query("""
       SELECT DISTINCT i FROM Instructor i
       JOIN i.courses c
       WHERE c.active = true
       """)
    List<Instructor> findInstructorsWithActiveCourses();


}
