package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.InstructorDto.InstructorRequest;
import edu.unimagdalena.lms2.dto.InstructorDto.InstructorResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface InstructorService {

    // --- CRUD ---
    InstructorResponse create(InstructorRequest request);
    InstructorResponse findById(UUID id);
    List<InstructorResponse> findAll();
    InstructorResponse update(UUID id, InstructorRequest request);
    void delete(UUID id);

    // --- Casos de uso ---
    InstructorResponse findByEmail(String email);
    InstructorResponse findByFullNameIgnoreCase(String fullName);
    List<InstructorResponse> findByCreatedAtAfter(Instant date);
    List<InstructorResponse> findTop10ByUpdatedAtAsc();
    List<InstructorResponse> findInstructorsWithProfile();
    List<InstructorResponse> findInstructorsWithActiveCourses();
}
