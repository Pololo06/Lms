package edu.unimagdalena.lms2.services;

import edu.unimagdalena.lms2.dto.CourseDto.CourseRequest;
import edu.unimagdalena.lms2.dto.CourseDto.CourseResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CourseService {

    // --- CRUD ---
    CourseResponse create(CourseRequest request);
    CourseResponse findById(UUID id);
    List<CourseResponse> findAll();
    CourseResponse update(UUID id, CourseRequest request);
    void delete(UUID id);

    // --- Casos de uso ---
    List<CourseResponse> findByTitle(String title);
    List<CourseResponse> findByStatus(String status);
    List<CourseResponse> findActiveCourses();
    List<CourseResponse> findInactiveCourses();
    List<CourseResponse> findByCreatedAt(Instant date);
    List<CourseResponse> findTop5RecentlyUpdated();
    List<CourseResponse> findAllCoursesWithInstructor();
}
