package edu.unimagdalena.lms2.repositories;


import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.entities.InstructorProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InstructorProfileRepositoryTest extends AbstractRepository {

    @Autowired
    private InstructorProfileRepository profileRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    private Instructor instructor;
    private InstructorProfile profile;

    @BeforeEach
    void setUp() {

        profileRepository.deleteAll();
        instructorRepository.deleteAll();

        // Instructor base
        instructor = instructorRepository.save(Instructor.builder()
                .fullName("Dr. Smith")
                .email("smith@test.com")
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-06-01T10:00:00Z"))
                .build());

        // Profile base
        profile = profileRepository.save(InstructorProfile.builder()
                .phone("3001234567")
                .bio("Profesor de Matemáticas")
                .instructor(instructor)
                .build());
    }

    // -------------------- CREATE --------------------
    @Test
    void shouldCreateInstructorProfile() {

        InstructorProfile nuevo = InstructorProfile.builder()
                .phone("3119998888")
                .bio("Profesor de Física")
                .instructor(instructor)
                .build();

        InstructorProfile saved = profileRepository.save(nuevo);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPhone()).isEqualTo("3119998888");
    }

    // -------------------- READ --------------------
    @Test
    void shouldReadProfileById() {

        UUID id = profile.getId();

        Optional<InstructorProfile> found = profileRepository.findById(id);

        assertThat(found).isPresent();
        assertThat(found.get().getPhone()).isEqualTo("3001234567");
    }

    // -------------------- UPDATE --------------------
    @Test
    void shouldUpdatePhone() {

        profile.setPhone("3000000000");

        InstructorProfile updated = profileRepository.save(profile);

        assertThat(updated.getPhone()).isEqualTo("3000000000");
    }

    // -------------------- DELETE --------------------
    @Test
    void shouldDeleteProfile() {

        UUID id = profile.getId();

        profileRepository.delete(profile);

        boolean exists = profileRepository.existsById(id);

        assertThat(exists).isFalse();
    }

    // -------------------- QUERY METHOD --------------------
    @Test
    void shouldFindByPhone() {

        Optional<InstructorProfile> found =
                profileRepository.findByPhone("3001234567");

        assertThat(found).isPresent();
        assertThat(found.get().getBio())
                .isEqualTo("Profesor de Matemáticas");
    }
}