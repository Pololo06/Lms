package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.EnrollmentDto.EnrollmentRequest;
import edu.unimagdalena.lms2.dto.EnrollmentDto.EnrollmentResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EnrollmentService {

    // --- CRUD ---
    EnrollmentResponse create(EnrollmentRequest request);
    EnrollmentResponse findById(UUID id);
    List<EnrollmentResponse> findAll();
    EnrollmentResponse update(UUID id, EnrollmentRequest request);
    void delete(UUID id);

    // --- Casos de uso ---
    List<EnrollmentResponse> findByStatus(String status);
    List<EnrollmentResponse> findByEnrolledAtBetween(Instant from, Instant to);
}
