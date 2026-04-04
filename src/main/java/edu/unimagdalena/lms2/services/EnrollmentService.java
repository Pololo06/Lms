package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.EnrollmentDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EnrollmentService {

    EnrollmentDto.EnrollmentResponse create(EnrollmentDto.EnrollmentRequest request);
    EnrollmentDto.EnrollmentResponse findById(UUID id);
    List<EnrollmentDto.EnrollmentResponse> findAll();
    EnrollmentDto.EnrollmentResponse update(UUID id, EnrollmentDto.EnrollmentUpdateRequest request);
    void delete(UUID id);

    List<EnrollmentDto.EnrollmentResponse> findByStatus(String status);
    List<EnrollmentDto.EnrollmentResponse> findByEnrolledAtBetween(Instant from, Instant to);
}
