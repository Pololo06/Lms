package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.EnrollmentDto.EnrollmentRequest;
import edu.unimagdalena.lms2.dto.EnrollmentDto.EnrollmentUpdateRequest;
import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Enrollment;
import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.entities.Student;
import edu.unimagdalena.lms2.repositories.CourseRepository;
import edu.unimagdalena.lms2.repositories.EnrollmentRepository;
import edu.unimagdalena.lms2.repositories.StudentRepository;
import edu.unimagdalena.lms2.services.implement.EnrollmentServiceImpl;
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
class EnrollmentServiceImplTest {

    @Mock
    EnrollmentRepository enrollmentRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    EnrollmentServiceImpl enrollmentService;

    @Test
    void create_deberiaGuardarMatriculaConStatusActive() {
        var studentId = UUID.randomUUID();
        var courseId = UUID.randomUUID();
        var enrollmentId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var student = Student.builder().id(studentId).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(courseId).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var req = new EnrollmentRequest(studentId, courseId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(inv -> {
            Enrollment e = inv.getArgument(0);
            e.setId(enrollmentId);
            return e;
        });

        var res = enrollmentService.create(req);

        assertThat(res.id()).isEqualTo(enrollmentId);
        assertThat(res.status()).isEqualTo("ACTIVE");
        assertThat(res.studentFullName()).isEqualTo("Juan Pérez");
        assertThat(res.courseTitle()).isEqualTo("Bases de Datos");
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void create_cuandoEstudianteNoExiste_deberiaLanzarExcepcion() {
        var studentId = UUID.randomUUID();
        var req = new EnrollmentRequest(studentId, UUID.randomUUID());
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.create(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(studentId.toString());
    }

    @Test
    void create_cuandoCursoNoExiste_deberiaLanzarExcepcion() {
        var studentId = UUID.randomUUID();
        var courseId = UUID.randomUUID();
        var student = Student.builder().id(studentId).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var req = new EnrollmentRequest(studentId, courseId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.create(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(courseId.toString());
    }

    @Test
    void findById_cuandoExiste_deberiaRetornarResponse() {
        var enrollmentId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var enrollment = Enrollment.builder().id(enrollmentId).status("ACTIVE")
                .enrolledAt(Instant.now()).student(student).course(course).build();
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));

        var res = enrollmentService.findById(enrollmentId);

        assertThat(res.id()).isEqualTo(enrollmentId);
    }

    @Test
    void findById_cuandoNoExiste_deberiaLanzarExcepcion() {
        var enrollmentId = UUID.randomUUID();
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.findById(enrollmentId))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void findAll_deberiaRetornarLista() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var enrollment = Enrollment.builder().id(UUID.randomUUID()).status("ACTIVE")
                .enrolledAt(Instant.now()).student(student).course(course).build();
        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));

        var res = enrollmentService.findAll();

        assertThat(res).hasSize(1);
    }

    @Test
    void update_deberiaCambiarStatus() {
        var enrollmentId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var existing = Enrollment.builder().id(enrollmentId).status("ACTIVE")
                .enrolledAt(Instant.now()).student(student).course(course).build();
        var req = new EnrollmentUpdateRequest("CANCELLED");
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(existing));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(inv -> inv.getArgument(0));

        var res = enrollmentService.update(enrollmentId, req);

        assertThat(res.status()).isEqualTo("CANCELLED");
    }

    @Test
    void delete_cuandoExiste_deberiaEliminar() {
        var enrollmentId = UUID.randomUUID();
        when(enrollmentRepository.existsById(enrollmentId)).thenReturn(true);

        enrollmentService.delete(enrollmentId);

        verify(enrollmentRepository).deleteById(enrollmentId);
    }

    @Test
    void findByStatus_deberiaRetornarMatriculasConEseStatus() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var enrollment = Enrollment.builder().id(UUID.randomUUID()).status("ACTIVE")
                .enrolledAt(Instant.now()).student(student).course(course).build();
        when(enrollmentRepository.findByStatus("ACTIVE")).thenReturn(List.of(enrollment));

        var res = enrollmentService.findByStatus("ACTIVE");

        assertThat(res).hasSize(1);
        assertThat(res.get(0).status()).isEqualTo("ACTIVE");
    }

    @Test
    void findByEnrolledAtBetween_deberiaRetornarLista() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var enrollment = Enrollment.builder().id(UUID.randomUUID()).status("ACTIVE")
                .enrolledAt(Instant.now()).student(student).course(course).build();
        var from = Instant.now().minusSeconds(3600);
        var to = Instant.now();
        when(enrollmentRepository.findByEnrolledAtBetween(from, to)).thenReturn(List.of(enrollment));

        var res = enrollmentService.findByEnrolledAtBetween(from, to);

        assertThat(res).hasSize(1);
    }
}
