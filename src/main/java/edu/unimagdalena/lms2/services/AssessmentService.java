package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.AssessmentDto.AssessmentRequest;
import edu.unimagdalena.lms2.dto.AssessmentDto.AssessmentResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AssessmentService {

    // --- CRUD ---
    AssessmentResponse create(AssessmentRequest request);
    AssessmentResponse findById(UUID id);
    List<AssessmentResponse> findAll();
    AssessmentResponse update(UUID id, AssessmentRequest request);
    void delete(UUID id);

    // --- Casos de uso ---
    List<AssessmentResponse> findByType(String type);
    List<AssessmentResponse> findByScoreGreaterThan(int score);
    List<AssessmentResponse> findByTakenAtBefore(Instant date);
    Long countAssessmentsByStudent(UUID studentId);
}
