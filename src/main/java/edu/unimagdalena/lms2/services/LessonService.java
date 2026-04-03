package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.LessonDto.LessonRequest;
import edu.unimagdalena.lms2.dto.LessonDto.LessonResponse;

import java.util.List;
import java.util.UUID;

public interface LessonService {

    // --- CRUD ---
    LessonResponse create(LessonRequest request);
    LessonResponse findById(UUID id);
    List<LessonResponse> findAll();
    LessonResponse update(UUID id, LessonRequest request);
    void delete(UUID id);

    // --- Casos de uso ---
    List<LessonResponse> findByTitle(String title);
    List<LessonResponse> findByOrderIndex(int orderIndex);
    List<LessonResponse> findLessonsByCourseId(UUID courseId);
}
