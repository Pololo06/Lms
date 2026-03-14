package edu.unimagdalena.lms2.repositories;

import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.entities.InstructorProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class InstructorRepositoryTest extends AbstractRepository {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorProfileRepository profileRepository;

    private Instructor instructor;
    private Course course1;
    private Course course2;

    @BeforeEach
    void setUp() {
        // Given: limpiar datos
        courseRepository.deleteAll();
        profileRepository.deleteAll();
        instructorRepository.deleteAll();

        // Crear instructor de prueba
        instructor = instructorRepository.save(Instructor.builder()
                .fullName("Dr. Smith")
                .email("smith@test.com")
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-06-01T10:00:00Z"))
                .build());

        // Crear profile
        InstructorProfile profile = profileRepository.save(InstructorProfile.builder()
                .bio("Profesor de Matemáticas")
                .instructor(instructor)
                .phone("123456789")
                .build());

        instructor.setInstructorProfile(profile);
        instructorRepository.save(instructor);

        // Crear cursos asociados
        course1 = Course.builder()
                .title("Matemáticas")
                .status("ACTIVE")
                .active(true)
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-06-01T10:00:00Z"))
                .instructor(instructor)
                .build();

        course2 = Course.builder()
                .title("Historia")
                .status("INACTIVE")
                .active(false)
                .createdAt(Instant.parse("2023-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-02-01T10:00:00Z"))
                .instructor(instructor)
                .build();

        courseRepository.save(course1);
        courseRepository.save(course2);
        instructor.getCourses().add(course1);
        instructor.getCourses().add(course2);
    }

    // -------------------- CREATE --------------------
    @Test
    void shouldCreateInstructor() {
        // Given
        Instructor nueva = Instructor.builder()
                .fullName("Dr. Jane")
                .email("jane@test.com")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When
        Instructor saved = instructorRepository.save(nueva);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFullName()).isEqualTo("Dr. Jane");
        assertThat(saved.getEmail()).isEqualTo("jane@test.com");
    }

    // -------------------- READ --------------------
    @Test
    void shouldReadInstructorById() {
        // Given
        UUID id = instructor.getId();

        // When
        Optional<Instructor> found = instructorRepository.findById(id);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Dr. Smith");
    }

    // -------------------- UPDATE --------------------
    @Test
    void shouldUpdateInstructorEmail() {
        // Given
        instructor.setEmail("smith_updated@test.com");

        // When
        Instructor updated = instructorRepository.save(instructor);

        // Then
        assertThat(updated.getEmail()).isEqualTo("smith_updated@test.com");
    }

    // -------------------- DELETE --------------------
    @Test
    void shouldDeleteInstructor() {
        // Given
        UUID id = instructor.getId();

        // When
        courseRepository.deleteAll();
        profileRepository.deleteAll();
        instructorRepository.delete(instructor);

        // Then
        boolean exists = instructorRepository.existsById(id);
        assertThat(exists).isFalse();
    }

    // -------------------- QUERIES DERIVADAS --------------------
    @Test
    void shouldFindByFullNameIgnoreCase() {
        // Given
        String name = "dr. smith";

        // When
        Optional<Instructor> found = instructorRepository.findByFullNameIgnoreCase(name);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Dr. Smith");
    }

    @Test
    void shouldFindByEmail() {
        // Given
        String email = "smith@test.com";

        // When
        Optional<Instructor> found = instructorRepository.findByEmail(email);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldFindByCreatedAtAfter() {
        // Given
        Instant date = Instant.parse("2023-06-01T00:00:00Z");

        // When
        List<Instructor> results = instructorRepository.findByCreatedAtAfter(date);

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getFullName()).isEqualTo("Dr. Smith");
    }

    @Test
    void shouldFindTop10ByUpdatedAtAsc() {
        // When
        List<Instructor> results = instructorRepository.findTop10ByOrderByUpdatedAtAsc();

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getFullName()).isEqualTo("Dr. Smith");
    }

    // -------------------- QUERY CON JOIN FETCH --------------------
    @Test
    void shouldFindInstructorsWithProfile() {
        // When
        List<Instructor> results = instructorRepository.findInstructorsWithProfile();

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getInstructorProfile()).isNotNull();
        assertThat(results.getFirst().getInstructorProfile().getBio()).isEqualTo("Profesor de Matemáticas");
    }

    @Test
    void shouldFindInstructorsWithActiveCourses() {

        // When
        List<Instructor> results = instructorRepository.findInstructorsWithActiveCourses();
        System.out.println(results.getFirst());
        System.out.println(results.getFirst().getCourses());

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getCourses())
                .anyMatch(Course::getActive);
    }
}