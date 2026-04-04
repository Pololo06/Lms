package edu.unimagdalena.lms2.services.implement;

import edu.unimagdalena.lms2.dto.LessonDto.LessonRequest;
import edu.unimagdalena.lms2.dto.LessonDto.LessonResponse;
import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Lesson;
import edu.unimagdalena.lms2.repositories.CourseRepository;
import edu.unimagdalena.lms2.repositories.LessonRepository;
import edu.unimagdalena.lms2.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;


    private LessonResponse toResponse(Lesson l) {
        return new LessonResponse(
                l.getId(),
                l.getTitle(),
                l.getOrderIndex(),
                l.getCourse().getId(),
                l.getCourse().getTitle()
        );
    }

    //CRUD

    @Override
    @Transactional
    public LessonResponse create(LessonRequest request) {
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + request.courseId()));

        Lesson lesson = Lesson.builder()
                .title(request.title())
                .orderIndex(request.orderIndex())
                .course(course)
                .build();

        return toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse findById(UUID id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lección no encontrada con id: " + id));
        return toResponse(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findAll() {
        return lessonRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public LessonResponse update(UUID id, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lección no encontrada con id: " + id));

        lesson.setTitle(request.title());
        lesson.setOrderIndex(request.orderIndex());

        if (request.courseId() != null) {
            Course course = courseRepository.findById(request.courseId())
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + request.courseId()));
            lesson.setCourse(course);
        }

        return toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!lessonRepository.existsById(id)) {
            throw new RuntimeException("Lección no encontrada con id: " + id);
        }
        lessonRepository.deleteById(id);
    }

    //Casos de uso

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findByTitle(String title) {
        return lessonRepository.findByTitle(title)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findByOrderIndex(int orderIndex) {
        return lessonRepository.findByOrderIndex(orderIndex)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> findLessonsByCourseId(UUID courseId) {
        return lessonRepository.findLessonsByCourseId(courseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
