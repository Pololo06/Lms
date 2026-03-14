package edu.unimagdalena.lms2.repositories;


import edu.unimagdalena.lms2.entities.InstructorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InstructorProfileRepository extends JpaRepository<InstructorProfile, UUID> {
    Optional<InstructorProfile> findByPhone(String phone);
}
