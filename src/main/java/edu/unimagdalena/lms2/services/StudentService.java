package edu.unimagdalena.lms2.services;


import edu.unimagdalena.lms2.dto.StudentDto.StudentResponse;
import edu.unimagdalena.lms2.dto.StudentDto.StudentRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface StudentService {

    StudentResponse create(StudentRequest request);
    StudentResponse findById(UUID id);
    List<StudentResponse> findAll();
    StudentResponse update(UUID id, StudentRequest request);
    void delete(UUID id);

    List<StudentResponse> findByFullName(String fullName);
    StudentResponse findByEmail(String email);
    List<StudentResponse> findByCreatedAtBetween(Instant from, Instant to);
    List<StudentResponse> findStudentsWithAssessments();
    List<StudentResponse> findStudentsWithMoreThanXCourses(Long minCourses);
    List<StudentResponse> findStudentsEnrolledInActiveCourses();
}
