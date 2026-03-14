package edu.unimagdalena.lms2.repositories;

import edu.unimagdalena.lms2.entities.Assessment;
import edu.unimagdalena.lms2.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AssessmentRepositoryTest extends AbstractRepository {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    void setUp() {
        // Given: un estudiante y tres assessments existentes
        assessmentRepository.deleteAll();

        student = studentRepository.save(Student.builder()
                .fullName("Juan Perez")
                .email("juan@test.com")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build());

        assessmentRepository.save(Assessment.builder()
                .type("QUIZ")
                .score(85)
                .takenAt(Instant.parse("2024-01-10T10:00:00Z"))
                .student(student)
                .build());

        assessmentRepository.save(Assessment.builder()
                .type("EXAM")
                .score(60)
                .takenAt(Instant.parse("2024-06-01T10:00:00Z"))
                .student(student)
                .build());

        assessmentRepository.save(Assessment.builder()
                .type("QUIZ")
                .score(40)
                .takenAt(Instant.parse("2025-01-01T10:00:00Z"))
                .student(student)
                .build());
    }

    // -------------------- CREATE --------------------
    @Test
    void shouldCreateAssessment() {
        // Given
        Assessment nueva = Assessment.builder()
                .type("EXAM")
                .score(90)
                .takenAt(Instant.now())
                .student(student)
                .build();

        // When
        Assessment saved = assessmentRepository.save(nueva);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getType()).isEqualTo("EXAM");
        assertThat(saved.getScore()).isEqualTo(90);
    }

    // -------------------- READ --------------------
    @Test
    void shouldReadAssessmentById() {
        // Given
        Assessment existing = assessmentRepository.findByType("QUIZ").get(0);

        // When
        Assessment found = assessmentRepository.findById(existing.getId()).orElse(null);

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getType()).isEqualTo(existing.getType());
    }

    // -------------------- UPDATE --------------------
    @Test
    void shouldUpdateAssessmentScore() {
        // Given
        Assessment existing = assessmentRepository.findByType("QUIZ").get(0);
        existing.setScore(100);

        // When
        Assessment updated = assessmentRepository.save(existing);

        // Then
        assertThat(updated.getScore()).isEqualTo(100);
    }

    // -------------------- DELETE --------------------
    @Test
    void shouldDeleteAssessment() {
        // Given
        Assessment toDelete = assessmentRepository.findByType("EXAM").get(0);

        // When
        assessmentRepository.delete(toDelete);

        // Then
        boolean exists = assessmentRepository.existsById(toDelete.getId());
        assertThat(exists).isFalse();
    }

    // -------------------- QUERY BY TYPE --------------------
    @Test
    void shouldFindAssessmentsByType() {
        // Given
        String type = "QUIZ";

        // When
        List<Assessment> quizzes = assessmentRepository.findByType(type);

        // Then
        assertThat(quizzes).hasSize(2);
        assertThat(quizzes).allMatch(a -> a.getType().equals("QUIZ"));
    }

    // -------------------- QUERY BY SCORE --------------------
    @Test
    void shouldFindAssessmentsWithScoreGreaterThan() {
        // Given
        int threshold = 70;

        // When
        List<Assessment> altos = assessmentRepository.findByScoreGreaterThan(threshold);

        // Then
        assertThat(altos).hasSize(1);
        assertThat(altos.get(0).getScore()).isGreaterThan(threshold);
    }

    // -------------------- QUERY BY DATE --------------------
    @Test
    void shouldFindAssessmentsBeforeDate() {
        // Given
        Instant date = Instant.parse("2025-01-01T00:00:00Z");

        // When
        List<Assessment> anteriores = assessmentRepository.findByTakenAtBefore(date);

        // Then
        assertThat(anteriores).hasSize(2);
        assertThat(anteriores).allMatch(a -> a.getTakenAt().isBefore(date));
    }

    // -------------------- COUNT BY STUDENT --------------------
    @Test
    void shouldCountAssessmentsByStudent() {
        // Given
        UUID studentId = student.getId();

        // When
        Long count = assessmentRepository.countAssessmentsByStudent(studentId);

        // Then
        assertThat(count).isEqualTo(3L);
    }
}