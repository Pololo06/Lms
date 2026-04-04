package edu.unimagdalena.lms2.services.implement;

import edu.unimagdalena.lms2.dto.InstructorDto.InstructorRequest;
import edu.unimagdalena.lms2.dto.InstructorDto.InstructorResponse;
import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.repositories.InstructorRepository;
import edu.unimagdalena.lms2.services.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    private InstructorResponse toResponse(Instructor i) {
        return new InstructorResponse(
                i.getId(),
                i.getFullName(),
                i.getEmail(),
                i.getCreatedAt(),
                i.getUpdatedAt()
        );
    }

    // CRUD

    @Override
    @Transactional
    public InstructorResponse create(InstructorRequest request) {
        Instructor instructor = Instructor.builder()
                .fullName(request.fullName())
                .email(request.email())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        return toResponse(instructorRepository.save(instructor));
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorResponse findById(UUID id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor no encontrado con id: " + id));
        return toResponse(instructor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorResponse> findAll() {
        return instructorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public InstructorResponse update(UUID id, InstructorRequest request) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor no encontrado con id: " + id));
        instructor.setFullName(request.fullName());
        instructor.setEmail(request.email());
        instructor.setUpdatedAt(Instant.now());
        return toResponse(instructorRepository.save(instructor));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!instructorRepository.existsById(id)) {
            throw new RuntimeException("Instructor no encontrado con id: " + id);
        }
        instructorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorResponse findByEmail(String email) {
        Instructor instructor = instructorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Instructor no encontrado con email: " + email));
        return toResponse(instructor);
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorResponse findByFullNameIgnoreCase(String fullName) {
        Instructor instructor = instructorRepository.findByFullNameIgnoreCase(fullName)
                .orElseThrow(() -> new RuntimeException("Instructor no encontrado con nombre: " + fullName));
        return toResponse(instructor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorResponse> findByCreatedAtAfter(Instant date) {
        return instructorRepository.findByCreatedAtAfter(date)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorResponse> findTop10ByUpdatedAtAsc() {
        return instructorRepository.findTop10ByOrderByUpdatedAtAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorResponse> findInstructorsWithProfile() {
        return instructorRepository.findInstructorsWithProfile()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstructorResponse> findInstructorsWithActiveCourses() {
        return instructorRepository.findInstructorsWithActiveCourses()
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
