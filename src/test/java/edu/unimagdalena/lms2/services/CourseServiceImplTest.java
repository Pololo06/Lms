package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.CourseDto.CourseRequest;
import edu.unimagdalena.lms2.dto.CourseDto.CourseResponse;
import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.repositories.CourseRepository;
import edu.unimagdalena.lms2.repositories.InstructorRepository;
import edu.unimagdalena.lms2.services.implement.CourseServiceImpl;
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
class CourseServiceImplTest {

    @Mock
    CourseRepository courseRepository;

    @Mock
    InstructorRepository instructorRepository;

    @InjectMocks
    CourseServiceImpl courseService;

    @Test
    void create_deberiaGuardarYRetornarResponse() {
        var instructorId = UUID.randomUUID();
        var courseId = UUID.randomUUID();
        var instructor = Instructor.builder().id(instructorId).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var req = new CourseRequest("Bases de Datos", "PUBLISHED", instructorId, true);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenAnswer(inv -> {
            Course c = inv.getArgument(0);
            c.setId(courseId);
            return c;
        });

        var res = courseService.create(req);

        assertThat(res.id()).isEqualTo(courseId);
        assertThat(res.title()).isEqualTo("Bases de Datos");
        assertThat(res.active()).isTrue();
        assertThat(res.instructorFullName()).isEqualTo("Carlos López");
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void create_cuandoInstructorNoExiste_deberiaLanzarExcepcion() {
        var instructorId = UUID.randomUUID();
        var req = new CourseRequest("Bases de Datos", "PUBLISHED", instructorId, true);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.create(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(instructorId.toString());
    }

    @Test
    void findById_cuandoExiste_deberiaRetornarResponse() {
        var courseId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(courseId).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        var res = courseService.findById(courseId);

        assertThat(res.id()).isEqualTo(courseId);
        assertThat(res.title()).isEqualTo("Bases de Datos");
    }

    @Test
    void findById_cuandoNoExiste_deberiaLanzarExcepcion() {
        var courseId = UUID.randomUUID();
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.findById(courseId))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void findAll_deberiaRetornarLista() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(courseRepository.findAll()).thenReturn(List.of(course));

        var res = courseService.findAll();

        assertThat(res).hasSize(1);
    }

    @Test
    void update_deberiaActualizarCampos() {
        var courseId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var existing = Course.builder().id(courseId).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var req = new CourseRequest("Curso Actualizado", "DRAFT", instructor.getId(), false);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existing));
        when(instructorRepository.findById(instructor.getId())).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenAnswer(inv -> inv.getArgument(0));

        var res = courseService.update(courseId, req);

        assertThat(res.title()).isEqualTo("Curso Actualizado");
        assertThat(res.active()).isFalse();
    }

    @Test
    void delete_cuandoExiste_deberiaEliminar() {
        var courseId = UUID.randomUUID();
        when(courseRepository.existsById(courseId)).thenReturn(true);

        courseService.delete(courseId);

        verify(courseRepository).deleteById(courseId);
    }

    @Test
    void findActiveCourses_deberiaRetornarSoloActivos() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(courseRepository.findByActiveTrue()).thenReturn(List.of(course));

        var res = courseService.findActiveCourses();

        assertThat(res).hasSize(1);
        assertThat(res.get(0).active()).isTrue();
    }

    @Test
    void findInactiveCourses_deberiaRetornarSoloInactivos() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var inactivo = Course.builder().id(UUID.randomUUID()).title("Inactivo").status("DRAFT")
                .active(false).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(courseRepository.findByActiveFalse()).thenReturn(List.of(inactivo));

        var res = courseService.findInactiveCourses();

        assertThat(res).hasSize(1);
        assertThat(res.get(0).active()).isFalse();
    }

    @Test
    void findByStatus_deberiaRetornarCursosConEseStatus() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(courseRepository.findByStatus("PUBLISHED")).thenReturn(List.of(course));

        var res = courseService.findByStatus("PUBLISHED");

        assertThat(res).hasSize(1);
        assertThat(res.get(0).status()).isEqualTo("PUBLISHED");
    }

    @Test
    void findTop5RecentlyUpdated_deberiaRetornarLista() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(courseRepository.findTop5ByOrderByUpdatedAtDesc()).thenReturn(List.of(course));

        var res = courseService.findTop5RecentlyUpdated();

        assertThat(res).isNotEmpty();
    }

    @Test
    void findAllCoursesWithInstructor_deberiaRetornarCursosConInstructor() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(courseRepository.findAllCoursesWithInstructor()).thenReturn(List.of(course));

        var res = courseService.findAllCoursesWithInstructor();

        assertThat(res).hasSize(1);
        assertThat(res.get(0).instructorFullName()).isEqualTo("Carlos López");
    }
}
