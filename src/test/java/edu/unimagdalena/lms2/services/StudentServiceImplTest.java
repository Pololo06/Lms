package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.StudentDto.StudentRequest;
import edu.unimagdalena.lms2.entities.Student;
import edu.unimagdalena.lms2.repositories.StudentRepository;
import edu.unimagdalena.lms2.services.implement.StudentServiceImpl;
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
class StudentServiceImplTest {

    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentServiceImpl studentService;

    @Test
    void create_deberiaGuardarYRetornarStudentResponse() {
        var id = UUID.randomUUID();
        var req = new StudentRequest("Juan Pérez", "juan@unimagdalena.edu.co", Instant.now(), Instant.now());
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> {
            Student s = inv.getArgument(0);
            s.setId(id);
            return s;
        });

        var res = studentService.create(req);

        assertThat(res.id()).isEqualTo(id);
        assertThat(res.fullName()).isEqualTo("Juan Pérez");
        assertThat(res.email()).isEqualTo("juan@unimagdalena.edu.co");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void findById_cuandoExiste_deberiaRetornarResponse() {
        var id = UUID.randomUUID();
        var student = Student.builder().id(id).fullName("Juan Pérez")
                .email("juan@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        var res = studentService.findById(id);

        assertThat(res.id()).isEqualTo(id);
        assertThat(res.fullName()).isEqualTo("Juan Pérez");
    }

    @Test
    void findById_cuandoNoExiste_deberiaLanzarExcepcion() {
        var id = UUID.randomUUID();
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.findById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void findAll_deberiaRetornarListaDeResponses() {
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(studentRepository.findAll()).thenReturn(List.of(student));

        var res = studentService.findAll();

        assertThat(res).hasSize(1);
        assertThat(res.get(0).email()).isEqualTo("juan@unimagdalena.edu.co");
    }

    @Test
    void update_cuandoExiste_deberiaActualizarYRetornar() {
        var id = UUID.randomUUID();
        var existing = Student.builder().id(id).fullName("Juan Pérez")
                .email("juan@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var req = new StudentRequest("Juan Actualizado", "nuevo@email.com", Instant.now(), Instant.now());
        when(studentRepository.findById(id)).thenReturn(Optional.of(existing));
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        var res = studentService.update(id, req);

        assertThat(res.fullName()).isEqualTo("Juan Actualizado");
        assertThat(res.email()).isEqualTo("nuevo@email.com");
    }

    @Test
    void delete_cuandoExiste_deberiaEliminar() {
        var id = UUID.randomUUID();
        when(studentRepository.existsById(id)).thenReturn(true);

        studentService.delete(id);

        verify(studentRepository).deleteById(id);
    }

    @Test
    void delete_cuandoNoExiste_deberiaLanzarExcepcion() {
        var id = UUID.randomUUID();
        when(studentRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> studentService.delete(id))
                .isInstanceOf(RuntimeException.class);
        verify(studentRepository, never()).deleteById(any());
    }

    @Test
    void findByEmail_cuandoExiste_deberiaRetornarResponse() {
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(studentRepository.findByEmail("juan@unimagdalena.edu.co")).thenReturn(Optional.of(student));

        var res = studentService.findByEmail("juan@unimagdalena.edu.co");

        assertThat(res.email()).isEqualTo("juan@unimagdalena.edu.co");
    }

    @Test
    void findStudentsWithAssessments_deberiaRetornarLista() {
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(studentRepository.findStudentsWithAssessments()).thenReturn(List.of(student));

        var res = studentService.findStudentsWithAssessments();

        assertThat(res).hasSize(1);
    }

    @Test
    void findStudentsEnrolledInActiveCourses_deberiaRetornarLista() {
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(studentRepository.findStudentsEnrolledInActiveCourses()).thenReturn(List.of(student));

        var res = studentService.findStudentsEnrolledInActiveCourses();

        assertThat(res).isNotEmpty();
    }

    @Test
    void findStudentsWithMoreThanXCourses_deberiaRetornarLista() {
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@unimagdalena.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        when(studentRepository.findStudentsWithMoreThanXCourses(2L)).thenReturn(List.of(student));

        var res = studentService.findStudentsWithMoreThanXCourses(2L);

        assertThat(res).hasSize(1);
    }
}
