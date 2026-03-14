package edu.unimagdalena.lms2.repositories;

import edu.unimagdalena.lms2.entities.Lesson;
import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Instructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LessonRepositoryTest extends AbstractRepository {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    private Course course;
    private Lesson lesson1;
    private Lesson lesson2;

    @BeforeEach
    void setUp() {

        lessonRepository.deleteAll();
        courseRepository.deleteAll();
        instructorRepository.deleteAll();

        Instructor instructor = instructorRepository.save(Instructor.builder()
                .fullName("Dr. Smith")
                .email("smith@test.com")
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-06-01T10:00:00Z"))
                .build());

        course = courseRepository.save(Course.builder()
                .title("Matemáticas")
                .status("ACTIVE")
                .active(true)
                .createdAt(Instant.parse("2024-01-01T10:00:00Z"))
                .updatedAt(Instant.parse("2024-06-01T10:00:00Z"))
                .instructor(instructor)
                .build());

        lesson1 = lessonRepository.save(Lesson.builder()
                .title("Introducción")
                .orderIndex(1)
                .course(course)
                .build());

        lesson2 = lessonRepository.save(Lesson.builder()
                .title("Álgebra")
                .orderIndex(2)
                .course(course)
                .build());
    }

    // -------------------- CREATE --------------------
    @Test
    void shouldCreateLesson() {

        Lesson lesson = Lesson.builder()
                .title("Geometría")
                .orderIndex(3)
                .course(course)
                .build();

        Lesson saved = lessonRepository.save(lesson);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Geometría");
    }

    // -------------------- READ --------------------
    @Test
    void shouldReadLessonById() {

        Optional<Lesson> found = lessonRepository.findById(lesson1.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Introducción");
    }

    // -------------------- UPDATE --------------------
    @Test
    void shouldUpdateLessonTitle() {

        lesson1.setTitle("Introducción Actualizada");

        Lesson updated = lessonRepository.save(lesson1);

        assertThat(updated.getTitle())
                .isEqualTo("Introducción Actualizada");
    }

    // -------------------- DELETE --------------------
    @Test
    void shouldDeleteLesson() {

        UUID id = lesson1.getId();

        lessonRepository.delete(lesson1);

        assertThat(lessonRepository.existsById(id)).isFalse();
    }

    // -------------------- QUERY METHODS --------------------
    @Test
    void shouldFindByOrderIndex() {

        List<Lesson> results = lessonRepository.findByOrderIndex(1);

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getTitle())
                .isEqualTo("Introducción");
    }

    @Test
    void shouldFindByTitle() {

        List<Lesson> results = lessonRepository.findByTitle("Álgebra");

        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getOrderIndex()).isEqualTo(2);
    }
    // -------------------- QUERY --------------------
    @Test
    void shouldFindLessonsByCourseId() {

        List<Lesson> results =
                lessonRepository.findLessonsByCourseId(course.getId());

        assertThat(results).hasSize(2);
    }
}