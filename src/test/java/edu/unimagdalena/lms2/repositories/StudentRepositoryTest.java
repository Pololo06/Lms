package edu.unimagdalena.lms2.repositories;

import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Enrollment;
import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.entities.Student;
import edu.unimagdalena.lms2.entities.Assessment;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class StudentRepositoryTest extends AbstractRepository {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private InstructorRepository instructorRepository;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {

        assessmentRepository.deleteAll();
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        instructorRepository.deleteAll();

        // Crear instructor
        Instructor instructor = instructorRepository.save(Instructor.builder()
                .fullName("Dr. Smith")
                .email("smith@test.com")
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-06-01T10:00:00Z"))
                .build());

        // Crear estudiantes
        student1 = studentRepository.save(Student.builder()
                .fullName("Juan Perez")
                .email("juan@test.com")
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-06-01T10:00:00Z"))
                .build());

        student2 = studentRepository.save(Student.builder()
                .fullName("Maria Lopez")
                .email("maria@test.com")
                .createdAt(Instant.parse("2024-02-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-06-01T10:00:00Z"))
                .build());

        // Crear cursos
        Course course1 = courseRepository.save(Course.builder()
                .title("Matemáticas")
                .status("ACTIVE")
                .active(true)
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-06-01T10:00:00Z"))
                .instructor(instructor)
                .build());

        Course course2 = courseRepository.save(Course.builder()
                .title("Historia")
                .status("INACTIVE")
                .active(false)
                .createdAt(Instant.parse("2023-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-02-01T10:00:00Z"))
                .instructor(instructor)
                .build());

        // Crear enrollments
        enrollmentRepository.save(Enrollment.builder()
                .student(student1)
                .course(course1)
                .status("ACTIVE")
                .enrolledAt(Instant.parse("2024-01-10T00:00:00Z"))
                .build());

        enrollmentRepository.save(Enrollment.builder()
                .student(student1)
                .course(course2)
                .status("INACTIVE")
                .enrolledAt(Instant.parse("2024-02-10T00:00:00Z"))
                .build());

        enrollmentRepository.save(Enrollment.builder()
                .student(student2)
                .course(course1)
                .status("ACTIVE")
                .enrolledAt(Instant.parse("2024-03-10T00:00:00Z"))
                .build());

        // Crear assessments
        assessmentRepository.save(Assessment.builder()
                .type("QUIZ")
                .score(85)
                .takenAt(Instant.parse("2024-01-10T10:00:00Z"))
                .student(student1)
                .build());

        assessmentRepository.save(Assessment.builder()
                .type("EXAM")
                .score(60)
                .takenAt(Instant.parse("2024-06-01T10:00:00Z"))
                .student(student2)
                .build());
    }
    // -------------------- CREATE --------------------

    @Test
    void shouldCreateStudent() {

        Student nuevo = Student.builder()
                .fullName("Pedro Gomez")
                .email("pedro@test.com")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        Student saved = studentRepository.save(nuevo);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("pedro@test.com");
    }

    // -------------------- READ --------------------

    @Test
    void shouldReadStudentById() {

        Optional<Student> found = studentRepository.findById(student1.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Juan Perez");
    }

    // -------------------- UPDATE --------------------

    @Test
    void shouldUpdateStudentEmail() {

        student1.setEmail("updated@test.com");

        Student updated = studentRepository.save(student1);

        assertThat(updated.getEmail()).isEqualTo("updated@test.com");
    }

    // -------------------- DELETE --------------------

    @Test
    void shouldDeleteStudent() {

        UUID id = student1.getId();

        enrollmentRepository.deleteAll();
        assessmentRepository.deleteAll();
        studentRepository.delete(student1);

        boolean exists = studentRepository.existsById(id);

        assertThat(exists).isFalse();
    }

    // -------------------- QUERY METHODS --------------------

    @Test
    void shouldFindByFullName() {

        List<Student> results = studentRepository.findByFullName("Juan Perez");

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getEmail()).isEqualTo("juan@test.com");
    }

    @Test
    void shouldFindByEmail() {

        Optional<Student> result = studentRepository.findByEmail("juan@test.com");

        assertThat(result).isPresent();
        assertThat(result.get().getFullName()).isEqualTo("Juan Perez");
    }

    @Test
    void shouldFindByCreatedAt() {

        Instant date = Instant.parse("2024-01-01T10:00:00Z");

        List<Student> results = studentRepository.findByCreatedAt(date);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getEmail()).isEqualTo("juan@test.com");
    }

    @Test
    void shouldFindByCreatedAtBetween() {

        Instant min = Instant.parse("2024-01-01T00:00:00Z");
        Instant max = Instant.parse("2024-02-15T00:00:00Z");

        List<Student> results = studentRepository.findByCreatedAtBetween(min, max);

        assertThat(results).hasSize(2);
    }

    // -------------------- JPQL QUERIES --------------------

    @Test
    void shouldFindStudentsWithAssessments() {

        List<Student> results = studentRepository.findStudentsWithAssessments();

        assertThat(results).isNotNull();
    }

    @Test
    void shouldFindStudentsWithMoreThanXCourses() {

        List<Student> results = studentRepository.findStudentsWithMoreThanXCourses(1L);

        assertThat(results).isNotNull();
    }

    @Test
    void shouldFindStudentsEnrolledInActiveCourses() {

        List<Student> results = studentRepository.findStudentsEnrolledInActiveCourses();

        assertThat(results).isNotNull();
    }
}
