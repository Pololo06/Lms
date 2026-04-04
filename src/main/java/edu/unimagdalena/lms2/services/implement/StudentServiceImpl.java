package edu.unimagdalena.lms2.services.implement;

import edu.unimagdalena.lms2.dto.StudentDto.StudentRequest;
import edu.unimagdalena.lms2.dto.StudentDto.StudentResponse;
import edu.unimagdalena.lms2.entities.Student;
import edu.unimagdalena.lms2.repositories.StudentRepository;
import edu.unimagdalena.lms2.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private StudentResponse toResponse(Student s) {
        return new StudentResponse(
                s.getId(),
                s.getFullName(),
                s.getEmail(),
                s.getCreatedAt(),
                s.getUpdatedAt()
        );
    }

    // CRUD

    @Override
    @Transactional
    public StudentResponse create(StudentRequest request) {
        Student student = Student.builder()
                .fullName(request.fullName())
                .email(request.email())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return toResponse(studentRepository.save(student));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse findById(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con id: " + id));
        return toResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> findAll() {
        return studentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public StudentResponse update(UUID id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con id: " + id));
        student.setFullName(request.fullName());
        student.setEmail(request.email());
        student.setUpdatedAt(Instant.now());
        return toResponse(studentRepository.save(student));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Estudiante no encontrado con id: " + id);
        }
        studentRepository.deleteById(id);
    }

    // Casos de uso

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> findByFullName(String fullName) {
        return studentRepository.findByFullName(fullName)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse findByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con email: " + email));
        return toResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> findByCreatedAtBetween(Instant from, Instant to) {
        return studentRepository.findByCreatedAtBetween(from, to)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> findStudentsWithAssessments() {
        return studentRepository.findStudentsWithAssessments()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> findStudentsWithMoreThanXCourses(Long minCourses) {
        return studentRepository.findStudentsWithMoreThanXCourses(minCourses)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> findStudentsEnrolledInActiveCourses() {
        return studentRepository.findStudentsEnrolledInActiveCourses()
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
