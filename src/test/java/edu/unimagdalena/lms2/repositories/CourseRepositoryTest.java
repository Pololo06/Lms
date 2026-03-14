package edu.unimagdalena.lms2.repositories;

import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Instructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;


class CourseRepositoryTest extends AbstractRepository {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    private Instructor instructor;

    @BeforeEach
    void setUp() {
        // Given: limpiar cursos y crear un instructor de prueba
        courseRepository.deleteAll();
        instructorRepository.deleteAll();

        instructor = instructorRepository.save(Instructor.builder()
                .fullName("Dr. Smith")
                .email("smith@test.com")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        // Crear cursos de prueba
        courseRepository.save(Course.builder()
                .title("Matemáticas")
                .status("ACTIVE")
                .active(true)
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-06-01T10:00:00Z"))
                .instructor(instructor)
                .build());

        courseRepository.save(Course.builder()
                .title("Historia")
                .status("INACTIVE")
                .active(false)
                .createdAt(Instant.parse("2023-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-02-01T10:00:00Z"))
                .instructor(instructor)
                .build());

        courseRepository.save(Course.builder()
                .title("Física")
                .status("ACTIVE")
                .active(true)
                .createdAt(Instant.parse("2025-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2025-02-01T10:00:00Z"))
                .instructor(instructor)
                .build());
    }

    // -------------------- CREATE --------------------
    @Test
    void shouldCreateCourse() {
        // Given
        Course nueva = Course.builder()
                .title("Química")
                .status("ACTIVE")
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .instructor(instructor)
                .build();

        // When
        Course saved = courseRepository.save(nueva);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Química");
        assertThat(saved.getInstructor()).isNotNull();
    }

    // -------------------- READ --------------------
    @Test
    void shouldReadCourseById() {
        // Given
        Course existing = courseRepository.findByTitle("Matemáticas").getFirst();

        // When
        Course found = courseRepository.findById(existing.getId()).orElse(null);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Matemáticas");
    }

    // -------------------- UPDATE --------------------
    @Test
    void shouldUpdateCourseStatus() {
        // Given
        Course existing = courseRepository.findByTitle("Historia").getFirst();
        existing.setStatus("ACTIVE");

        // When
        Course updated = courseRepository.save(existing);

        // Then
        assertThat(updated.getStatus()).isEqualTo("ACTIVE");
    }

    // -------------------- DELETE --------------------
    @Test
    void shouldDeleteCourse() {
        // Given
        Course toDelete = courseRepository.findByTitle("Física").getFirst();

        // When
        courseRepository.delete(toDelete);

        // Then
        boolean exists = courseRepository.existsById(toDelete.getId());
        assertThat(exists).isFalse();
    }

    // -------------------- QUERIES DERIVADAS --------------------
    @Test
    void shouldFindByTitle() {
        // Given
        String title = "Matemáticas";

        // When
        List<Course> results = courseRepository.findByTitle(title);

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getTitle()).isEqualTo(title);
    }

    @Test
    void shouldFindByStatus() {
        // Given
        String status = "ACTIVE";

        // When
        List<Course> results = courseRepository.findByStatus(status);

        // Then
        assertThat(results).hasSize(2);
        assertThat(results).allMatch(c -> c.getStatus().equals("ACTIVE"));
    }

    @Test
    void shouldFindActiveCourses() {
        // When
        List<Course> results = courseRepository.findByActiveTrue();

        // Then
        assertThat(results).hasSize(2);
        assertThat(results).allMatch(Course::getActive);
    }

    @Test
    void shouldFindInactiveCourses() {
        // When
        List<Course> results = courseRepository.findByActiveFalse();

        // Then
        assertThat(results).hasSize(1);
        assertThat(results).allMatch(c -> !c.getActive());
    }

    @Test
    void shouldFindCoursesByCreatedAt() {
        // Given
        Instant date = Instant.parse("2025-01-01T10:00:00Z");

        // When
        List<Course> results = courseRepository.findByCreatedAt(date);

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getTitle()).isEqualTo("Física");
    }

    @Test
    void shouldFindTop5ByUpdatedAtDesc() {
        // When
        List<Course> results = courseRepository.findTop5ByOrderByUpdatedAtDesc();

        // Then
        assertThat(results).hasSize(3); // solo tenemos 3 cursos
        assertThat(results.getFirst().getTitle()).isEqualTo("Física"); // más reciente
    }

    // -------------------- QUERY CON @QUERY --------------------
    @Test
    void shouldFindAllCoursesWithInstructor() {
        // When
        List<Course> results = courseRepository.findAllCoursesWithInstructor();

        // Then
        assertThat(results).hasSize(3);
        assertThat(results).allMatch(c -> c.getInstructor() != null);
    }
}
