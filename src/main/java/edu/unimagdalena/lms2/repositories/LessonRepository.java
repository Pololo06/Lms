package edu.unimagdalena.lms2.repositories;

import edu.unimagdalena.lms2.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    List<Lesson> findByOrderIndex(int orderIndex);

    List<Lesson> findByTitle(String title);

    @Query("""
       SELECT l
       FROM Lesson l
       WHERE l.course.id = :courseId
       """)
    List<Lesson> findLessonsByCourseId(@Param("courseId") UUID courseId);
}