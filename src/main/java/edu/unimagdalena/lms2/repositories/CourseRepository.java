package edu.unimagdalena.lms2.repositories;

import edu.unimagdalena.lms2.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    List<Course> findByTitle(String title);

    List<Course> findByStatus(String status);

    List<Course> findByActiveTrue();

    List<Course> findByActiveFalse();

    List<Course> findByCreatedAt (Instant date);

    List<Course> findTop5ByOrderByUpdatedAtDesc();

    @Query("""
       SELECT c
       FROM Course c
       JOIN FETCH c.instructor
       """)
    List<Course> findAllCoursesWithInstructor();
}
