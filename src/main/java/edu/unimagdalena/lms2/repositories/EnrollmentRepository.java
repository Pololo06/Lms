package edu.unimagdalena.lms2.repositories;

import edu.unimagdalena.lms2.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> findByStatus(String status);

    List<Enrollment> findByEnrolledAtBetween(Instant MinLimit, Instant MaxLimit);
}
