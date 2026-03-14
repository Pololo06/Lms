package edu.unimagdalena.lms2.repositories;

import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Enrollment;
import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EnrollmentRepositoryTest extends AbstractRepository {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private InstructorRepository instructorRepository;

    private Course course1;
    private Course course2;
    private Student student;
    private Enrollment enrollment;

    @BeforeEach
    public void setup() {
        // Given:
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        instructorRepository.deleteAll();

        Instructor instructor = instructorRepository.save(Instructor.builder()
                .fullName("Dr. Smith")
                .email("smith@test.com")
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-06-01T10:00:00Z"))
                .build());

        course1 = courseRepository.save(Course.builder()
                .title("Matemáticas")
                .status("ACTIVE")
                .active(true)
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-06-01T10:00:00Z"))
                .instructor(instructor)
                .build());

        course2 = courseRepository.save(Course.builder()
                .title("Historia")
                .status("INACTIVE")
                .active(false)
                .createdAt(Instant.parse("2023-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-02-01T10:00:00Z"))
                .instructor(instructor)
                .build());

        student = studentRepository.save(Student.builder()
                .fullName("Juan Perez")
                .email("juan@test.com")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        enrollment = enrollmentRepository.save(Enrollment.builder()
                .enrolledAt(Instant.parse("2024-01-01T00:00:00Z"))
                .status("ACTIVE")
                .course(course1)
                .student(student)
                .build());

        enrollmentRepository.save(Enrollment.builder()
                .enrolledAt(Instant.parse("2024-02-01T00:00:00Z"))
                .status("INACTIVE")
                .course(course1)
                .student(student)
                .build());

        enrollmentRepository.save(Enrollment.builder()
                .enrolledAt(Instant.parse("2024-03-01T00:00:00Z"))
                .status("INACTIVE")
                .course(course2)
                .student(student)
                .build());
    }

    // -------------------- CREATE --------------------
    @Test
    void shouldCreateEnrollment() {
        // Given
        Enrollment nueva = Enrollment.builder()
                .enrolledAt(Instant.parse("2024-05-01T00:00:00Z"))
                .status("ACTIVE")
                .course(course2)
                .student(student)
                .build();

        // When
        Enrollment saved = enrollmentRepository.save(nueva);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo("ACTIVE");
        assertThat(saved.getCourse().getTitle()).isEqualTo("Historia");
    }

    // -------------------- READ --------------------
    @Test
    void shouldReadEnrollmentById() {
        // Given
        UUID id = enrollment.getId();

        // When
        Optional<Enrollment> found = enrollmentRepository.findById(id);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getStatus()).isEqualTo("ACTIVE");
        assertThat(found.get().getCourse().getTitle()).isEqualTo("Matemáticas");
    }

    // -------------------- UPDATE --------------------
    @Test
    void shouldUpdateEnrollmentStatus() {
        // Given
        enrollment.setStatus("INACTIVE");

        // When
        Enrollment updated = enrollmentRepository.save(enrollment);

        // Then
        assertThat(updated.getStatus()).isEqualTo("INACTIVE");
    }

    // -------------------- DELETE --------------------
    @Test
    void shouldDeleteEnrollment() {
        // Given
        UUID id = enrollment.getId();

        // When
        enrollmentRepository.delete(enrollment);

        // Then
        boolean exists = enrollmentRepository.existsById(id);
        assertThat(exists).isFalse();
    }
    // -------------------- QUERY METHODS  --------------------
    @Test
    void shouldFindByStatus() {
        // Given
        String status = "ACTIVE";

        // When
        List<Enrollment> results = enrollmentRepository.findByStatus(status);

        // Then
        assertThat(results).hasSize(1);
        assertThat(results).allMatch(c -> c.getStatus().equals("ACTIVE"));
    }
    @Test
    void shouldFindEnrollmentsBetweenDates() {

        // When
        List<Enrollment> results =
                enrollmentRepository.findByEnrolledAtBetween(
                        Instant.parse("2024-01-15T00:00:00Z"),
                        Instant.parse("2024-02-15T00:00:00Z"));

        // Then
        assertThat(results).hasSize(1);
    }
}