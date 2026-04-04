package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.LessonDto.LessonRequest;
import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.entities.Lesson;
import edu.unimagdalena.lms2.repositories.CourseRepository;
import edu.unimagdalena.lms2.repositories.LessonRepository;
import edu.unimagdalena.lms2.services.implement.LessonServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @Mock
    LessonRepository lessonRepository;

    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    LessonServiceImpl lessonService;

    @Test
    void create_deberiaGuardarYRetornarResponse() {
        var courseId = UUID.randomUUID();
        var lessonId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(courseId).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var req = new LessonRequest("Introducción a SQL", 1, courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(inv -> {
            Lesson l = inv.getArgument(0);
            l.setId(lessonId);
            return l;
        });

        var res = lessonService.create(req);

        assertThat(res.id()).isEqualTo(lessonId);
        assertThat(res.title()).isEqualTo("Introducción a SQL");
        assertThat(res.orderIndex()).isEqualTo(1);
        assertThat(res.courseTitle()).isEqualTo("Bases de Datos");
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void create_cuandoCursoNoExiste_deberiaLanzarExcepcion() {
        var courseId = UUID.randomUUID();
        var req = new LessonRequest("SQL", 1, courseId);
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.create(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(courseId.toString());
    }

    @Test
    void findById_cuandoExiste_deberiaRetornarResponse() {
        var lessonId = UUID.randomUUID();
        var courseId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(courseId).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var lesson = Lesson.builder().id(lessonId).title("Introducción a SQL").orderIndex(1).course(course).build();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        var res = lessonService.findById(lessonId);

        assertThat(res.id()).isEqualTo(lessonId);
        assertThat(res.title()).isEqualTo("Introducción a SQL");
    }

    @Test
    void findById_cuandoNoExiste_deberiaLanzarExcepcion() {
        var lessonId = UUID.randomUUID();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.findById(lessonId))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void findAll_deberiaRetornarLista() {
        var courseId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(courseId).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var lesson = Lesson.builder().id(UUID.randomUUID()).title("Introducción a SQL").orderIndex(1).course(course).build();
        when(lessonRepository.findAll()).thenReturn(List.of(lesson));

        var res = lessonService.findAll();

        assertThat(res).hasSize(1);
    }

    @Test
    void update_deberiaActualizarCampos() {
        var lessonId = UUID.randomUUID();
        var courseId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(courseId).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var existing = Lesson.builder().id(lessonId).title("Introducción a SQL").orderIndex(1).course(course).build();
        var req = new LessonRequest("SQL Avanzado", 2, courseId);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(existing));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenAnswer(inv -> inv.getArgument(0));

        var res = lessonService.update(lessonId, req);

        assertThat(res.title()).isEqualTo("SQL Avanzado");
        assertThat(res.orderIndex()).isEqualTo(2);
    }

    @Test
    void delete_cuandoExiste_deberiaEliminar() {
        var lessonId = UUID.randomUUID();
        when(lessonRepository.existsById(lessonId)).thenReturn(true);

        lessonService.delete(lessonId);

        verify(lessonRepository).deleteById(lessonId);
    }

    @Test
    void findByTitle_deberiaRetornarLeccionesConEseTitulo() {
        var courseId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(courseId).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var lesson = Lesson.builder().id(UUID.randomUUID()).title("Introducción a SQL").orderIndex(1).course(course).build();
        when(lessonRepository.findByTitle("Introducción a SQL")).thenReturn(List.of(lesson));

        var res = lessonService.findByTitle("Introducción a SQL");

        assertThat(res).hasSize(1);
        assertThat(res.get(0).title()).isEqualTo("Introducción a SQL");
    }

    @Test
    void findByOrderIndex_deberiaRetornarLista() {
        var courseId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(courseId).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var lesson = Lesson.builder().id(UUID.randomUUID()).title("Introducción a SQL").orderIndex(1).course(course).build();
        when(lessonRepository.findByOrderIndex(1)).thenReturn(List.of(lesson));

        var res = lessonService.findByOrderIndex(1);

        assertThat(res).hasSize(1);
        assertThat(res.get(0).orderIndex()).isEqualTo(1);
    }

    @Test
    void findLessonsByCourseId_deberiaRetornarLeccionesDelCurso() {
        var courseId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(courseId).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var lesson = Lesson.builder().id(UUID.randomUUID()).title("Introducción a SQL").orderIndex(1).course(course).build();
        when(lessonRepository.findLessonsByCourseId(courseId)).thenReturn(List.of(lesson));

        var res = lessonService.findLessonsByCourseId(courseId);

        assertThat(res).hasSize(1);
        assertThat(res.get(0).courseId()).isEqualTo(courseId);
    }
}
