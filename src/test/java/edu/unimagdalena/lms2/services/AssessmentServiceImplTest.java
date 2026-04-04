package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.AssessmentDto.AssessmentRequest;
import edu.unimagdalena.lms2.entities.Assessment;
import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.entities.Student;
import edu.unimagdalena.lms2.repositories.AssessmentRepository;
import edu.unimagdalena.lms2.repositories.CourseRepository;
import edu.unimagdalena.lms2.repositories.StudentRepository;
import edu.unimagdalena.lms2.services.implement.AssessmentServiceImpl;
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
class AssessmentServiceImplTest {

    @Mock
    AssessmentRepository assessmentRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    CourseRepository courseRepository;

    @InjectMocks
    AssessmentServiceImpl assessmentService;

    @Test
    void create_deberiaGuardarYRetornarResponse() {
        var studentId = UUID.randomUUID();
        var courseId = UUID.randomUUID();
        var assessmentId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var student = Student.builder().id(studentId).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(courseId).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var req = new AssessmentRequest("EXAMEN", 85, Instant.now(), courseId, studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(assessmentRepository.save(any(Assessment.class))).thenAnswer(inv -> {
            Assessment a = inv.getArgument(0);
            a.setId(assessmentId);
            return a;
        });

        var res = assessmentService.create(req);

        assertThat(res.id()).isEqualTo(assessmentId);
        assertThat(res.type()).isEqualTo("EXAMEN");
        assertThat(res.score()).isEqualTo(85);
        assertThat(res.studentFullName()).isEqualTo("Juan Pérez");
        assertThat(res.courseTitle()).isEqualTo("Bases de Datos");
        verify(assessmentRepository).save(any(Assessment.class));
    }

    @Test
    void create_cuandoEstudianteNoExiste_deberiaLanzarExcepcion() {
        var studentId = UUID.randomUUID();
        var req = new AssessmentRequest("EXAMEN", 85, Instant.now(), UUID.randomUUID(), studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assessmentService.create(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(studentId.toString());
    }

    @Test
    void create_cuandoCursoNoExiste_deberiaLanzarExcepcion() {
        var studentId = UUID.randomUUID();
        var courseId = UUID.randomUUID();
        var student = Student.builder().id(studentId).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var req = new AssessmentRequest("EXAMEN", 85, Instant.now(), courseId, studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assessmentService.create(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(courseId.toString());
    }

    @Test
    void findById_cuandoExiste_deberiaRetornarResponse() {
        var assessmentId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var assessment = Assessment.builder().id(assessmentId).type("EXAMEN").score(85)
                .takenAt(Instant.now()).student(student).course(course).build();
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(assessment));

        var res = assessmentService.findById(assessmentId);

        assertThat(res.id()).isEqualTo(assessmentId);
        assertThat(res.score()).isEqualTo(85);
    }

    @Test
    void findById_cuandoNoExiste_deberiaLanzarExcepcion() {
        var assessmentId = UUID.randomUUID();
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assessmentService.findById(assessmentId))
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
        var assessment = Assessment.builder().id(UUID.randomUUID()).type("EXAMEN").score(85)
                .takenAt(Instant.now()).student(student).course(course).build();
        when(assessmentRepository.findAll()).thenReturn(List.of(assessment));

        var res = assessmentService.findAll();

        assertThat(res).hasSize(1);
    }

    @Test
    void update_deberiaActualizarCampos() {
        var assessmentId = UUID.randomUUID();
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var existing = Assessment.builder().id(assessmentId).type("EXAMEN").score(85)
                .takenAt(Instant.now()).student(student).course(course).build();
        var req = new AssessmentRequest("QUIZ", 90, Instant.now(), course.getId(), student.getId());
        when(assessmentRepository.findById(assessmentId)).thenReturn(Optional.of(existing));
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(assessmentRepository.save(any(Assessment.class))).thenAnswer(inv -> inv.getArgument(0));

        var res = assessmentService.update(assessmentId, req);

        assertThat(res.type()).isEqualTo("QUIZ");
        assertThat(res.score()).isEqualTo(90);
    }

    @Test
    void delete_cuandoExiste_deberiaEliminar() {
        var assessmentId = UUID.randomUUID();
        when(assessmentRepository.existsById(assessmentId)).thenReturn(true);

        assessmentService.delete(assessmentId);

        verify(assessmentRepository).deleteById(assessmentId);
    }

    @Test
    void findByType_deberiaRetornarEvaluacionesDeEseTipo() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var assessment = Assessment.builder().id(UUID.randomUUID()).type("EXAMEN").score(85)
                .takenAt(Instant.now()).student(student).course(course).build();
        when(assessmentRepository.findByType("EXAMEN")).thenReturn(List.of(assessment));

        var res = assessmentService.findByType("EXAMEN");

        assertThat(res).hasSize(1);
        assertThat(res.get(0).type()).isEqualTo("EXAMEN");
    }

    @Test
    void findByScoreGreaterThan_deberiaRetornarEvaluacionesConNotaAlta() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var assessment = Assessment.builder().id(UUID.randomUUID()).type("EXAMEN").score(85)
                .takenAt(Instant.now()).student(student).course(course).build();
        when(assessmentRepository.findByScoreGreaterThan(70)).thenReturn(List.of(assessment));

        var res = assessmentService.findByScoreGreaterThan(70);

        assertThat(res).hasSize(1);
        assertThat(res.get(0).score()).isGreaterThan(70);
    }

    @Test
    void findByTakenAtBefore_deberiaRetornarLista() {
        var instructor = Instructor.builder().id(UUID.randomUUID()).fullName("Carlos López")
                .email("carlos@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var student = Student.builder().id(UUID.randomUUID()).fullName("Juan Pérez")
                .email("juan@uni.edu.co").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var course = Course.builder().id(UUID.randomUUID()).title("Bases de Datos").status("PUBLISHED")
                .active(true).instructor(instructor).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var assessment = Assessment.builder().id(UUID.randomUUID()).type("EXAMEN").score(85)
                .takenAt(Instant.now()).student(student).course(course).build();
        var fecha = Instant.now().plusSeconds(3600);
        when(assessmentRepository.findByTakenAtBefore(fecha)).thenReturn(List.of(assessment));

        var res = assessmentService.findByTakenAtBefore(fecha);

        assertThat(res).hasSize(1);
    }

    @Test
    void countAssessmentsByStudent_deberiaRetornarConteo() {
        var studentId = UUID.randomUUID();
        when(studentRepository.existsById(studentId)).thenReturn(true);
        when(assessmentRepository.countAssessmentsByStudent(studentId)).thenReturn(3L);

        var count = assessmentService.countAssessmentsByStudent(studentId);

        assertThat(count).isEqualTo(3L);
    }

    @Test
    void countAssessmentsByStudent_cuandoEstudianteNoExiste_deberiaLanzarExcepcion() {
        var studentId = UUID.randomUUID();
        when(studentRepository.existsById(studentId)).thenReturn(false);

        assertThatThrownBy(() -> assessmentService.countAssessmentsByStudent(studentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(studentId.toString());
    }
}
