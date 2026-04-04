package edu.unimagdalena.lms2.services.implement;

import edu.unimagdalena.lms2.dto.EnrollmentDto;
import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Enrollment;
import edu.unimagdalena.lms2.entities.Student;
import edu.unimagdalena.lms2.repositories.CourseRepository;
import edu.unimagdalena.lms2.repositories.EnrollmentRepository;
import edu.unimagdalena.lms2.repositories.StudentRepository;
import edu.unimagdalena.lms2.services.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    private EnrollmentDto.EnrollmentResponse toResponse(Enrollment e) {
        return new EnrollmentDto.EnrollmentResponse(
                e.getId(),
                e.getStatus(),
                e.getEnrolledAt(),
                e.getStudent().getId(),
                e.getStudent().getFullName(),
                e.getCourse().getId(),
                e.getCourse().getTitle()
        );
    }

    @Override
    @Transactional
    public EnrollmentDto.EnrollmentResponse create(EnrollmentDto.EnrollmentRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con id: " + request.studentId()));
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + request.courseId()));
        Enrollment enrollment = Enrollment.builder()
                .status("ACTIVE")
                .enrolledAt(Instant.now())
                .student(student)
                .course(course)
                .build();
        return toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentDto.EnrollmentResponse findById(UUID id) {
        return toResponse(enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada con id: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDto.EnrollmentResponse> findAll() {
        return enrollmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public EnrollmentDto.EnrollmentResponse update(UUID id, EnrollmentDto.EnrollmentUpdateRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada con id: " + id));
        enrollment.setStatus(request.status());
        return toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!enrollmentRepository.existsById(id))
            throw new RuntimeException("Matrícula no encontrada con id: " + id);
        enrollmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDto.EnrollmentResponse> findByStatus(String status) {
        return enrollmentRepository.findByStatus(status).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentDto.EnrollmentResponse> findByEnrolledAtBetween(Instant from, Instant to) {
        return enrollmentRepository.findByEnrolledAtBetween(from, to).stream().map(this::toResponse).toList();
    }
}
