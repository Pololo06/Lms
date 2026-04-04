package edu.unimagdalena.lms2.services.implement;

import edu.unimagdalena.lms2.dto.AssessmentDto.AssessmentRequest;
import edu.unimagdalena.lms2.dto.AssessmentDto.AssessmentResponse;
import edu.unimagdalena.lms2.entities.Assessment;
import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Student;
import edu.unimagdalena.lms2.repositories.AssessmentRepository;
import edu.unimagdalena.lms2.repositories.CourseRepository;
import edu.unimagdalena.lms2.repositories.StudentRepository;
import edu.unimagdalena.lms2.services.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    private AssessmentResponse toResponse(Assessment a) {
        return new AssessmentResponse(
                a.getId(),
                a.getType(),
                a.getScore(),
                a.getTakenAt(),
                a.getCourse().getId(),
                a.getCourse().getTitle(),
                a.getStudent().getId(),
                a.getStudent().getFullName()
        );
    }

    // CRUD

    @Override
    @Transactional
    public AssessmentResponse create(AssessmentRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con id: " + request.studentId()));
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + request.courseId()));

        Assessment assessment = Assessment.builder()
                .type(request.type())
                .score(request.score())
                .takenAt(request.takenAt() != null ? request.takenAt() : Instant.now())
                .student(student)
                .course(course)
                .build();

        return toResponse(assessmentRepository.save(assessment));
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentResponse findById(UUID id) {
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada con id: " + id));
        return toResponse(assessment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findAll() {
        return assessmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AssessmentResponse update(UUID id, AssessmentRequest request) {
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada con id: " + id));

        assessment.setType(request.type());
        assessment.setScore(request.score());
        if (request.takenAt() != null) assessment.setTakenAt(request.takenAt());

        if (request.studentId() != null) {
            Student student = studentRepository.findById(request.studentId())
                    .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con id: " + request.studentId()));
            assessment.setStudent(student);
        }

        if (request.courseId() != null) {
            Course course = courseRepository.findById(request.courseId())
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + request.courseId()));
            assessment.setCourse(course);
        }

        return toResponse(assessmentRepository.save(assessment));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!assessmentRepository.existsById(id)) {
            throw new RuntimeException("Evaluación no encontrada con id: " + id);
        }
        assessmentRepository.deleteById(id);
    }

    // Casos de uso

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findByType(String type) {
        return assessmentRepository.findByType(type)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findByScoreGreaterThan(int score) {
        return assessmentRepository.findByScoreGreaterThan(score)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findByTakenAtBefore(Instant date) {
        return assessmentRepository.findByTakenAtBefore(date)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAssessmentsByStudent(UUID studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Estudiante no encontrado con id: " + studentId);
        }
        return assessmentRepository.countAssessmentsByStudent(studentId);
    }
}
