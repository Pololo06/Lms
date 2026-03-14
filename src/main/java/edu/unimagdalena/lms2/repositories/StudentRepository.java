package edu.unimagdalena.lms2.repositories;

import edu.unimagdalena.lms2.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    List<Student> findByFullName(String fullName);

    Optional<Student> findByEmail(String email);

    List<Student> findByCreatedAtBetween(Instant MinLimit, Instant MaxLimit);

    List<Student> findByCreatedAt(Instant date);

    @Query("""
       SELECT DISTINCT s
       FROM Student s
       JOIN s.assessments a
       """)
    List<Student> findStudentsWithAssessments();

    @Query("""
       SELECT s
       FROM Student s
       JOIN s.enrollments e
       GROUP BY s
       HAVING COUNT(e) > :minCourses
       """)
    List<Student> findStudentsWithMoreThanXCourses(@Param("minCourses") Long minCourses);

    @Query("""
       SELECT DISTINCT s
       FROM Student s
       JOIN s.enrollments e
       JOIN e.course c
       WHERE c.active = true
       """)
    List<Student> findStudentsEnrolledInActiveCourses();
 }
