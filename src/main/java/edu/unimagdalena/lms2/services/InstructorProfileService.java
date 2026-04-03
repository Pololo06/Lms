package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.InstructorProfileDto.InstructorProfileRequest;
import edu.unimagdalena.lms2.dto.InstructorProfileDto.InstructorProfileResponse;

import java.util.List;
import java.util.UUID;

public interface InstructorProfileService {

    // --- CRUD ---
    InstructorProfileResponse create(InstructorProfileRequest request);
    InstructorProfileResponse findById(UUID id);
    List<InstructorProfileResponse> findAll();
    InstructorProfileResponse update(UUID id, InstructorProfileRequest request);
    void delete(UUID id);

    // --- Casos de uso ---
    InstructorProfileResponse findByPhone(String phone);
}
