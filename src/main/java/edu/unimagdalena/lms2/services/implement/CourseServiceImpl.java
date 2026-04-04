package edu.unimagdalena.lms2.services.implement;

import edu.unimagdalena.lms2.dto.CourseDto.CourseRequest;
import edu.unimagdalena.lms2.dto.CourseDto.CourseResponse;
import edu.unimagdalena.lms2.entities.Course;
import edu.unimagdalena.lms2.entities.Instructor;
import edu.unimagdalena.lms2.repositories.CourseRepository;
import edu.unimagdalena.lms2.repositories.InstructorRepository;
import edu.unimagdalena.lms2.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;


    private CourseResponse toResponse(Course c) {
        return new CourseResponse(
                c.getId(),
                c.getTitle(),
                c.getStatus(),
                c.getActive(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                c.getInstructor().getId(),
                c.getInstructor().getFullName()
        );
    }

    //CRUD

    @Override
    @Transactional
    public CourseResponse create(CourseRequest request) {
        Instructor instructor = instructorRepository.findById(request.instructorId())
                .orElseThrow(() -> new RuntimeException("Instructor no encontrado con id: " + request.instructorId()));

        Course course = Course.builder()
                .title(request.title())
                .status(request.status())
                .active(request.active() != null ? request.active() : true)
                .instructor(instructor)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return toResponse(courseRepository.save(course));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse findById(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + id));
        return toResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findAll() {
        return courseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CourseResponse update(UUID id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + id));

        if (request.instructorId() != null) {
            Instructor instructor = instructorRepository.findById(request.instructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor no encontrado con id: " + request.instructorId()));
            course.setInstructor(instructor);
        }

        course.setTitle(request.title());
        course.setStatus(request.status());
        if (request.active() != null) course.setActive(request.active());
        course.setUpdatedAt(Instant.now());

        return toResponse(courseRepository.save(course));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Curso no encontrado con id: " + id);
        }
        courseRepository.deleteById(id);
    }

    //  Casos de uso

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findByTitle(String title) {
        return courseRepository.findByTitle(title)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findByStatus(String status) {
        return courseRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findActiveCourses() {
        return courseRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findInactiveCourses() {
        return courseRepository.findByActiveFalse()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findByCreatedAt(Instant date) {
        return courseRepository.findByCreatedAt(date)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findTop5RecentlyUpdated() {
        return courseRepository.findTop5ByOrderByUpdatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findAllCoursesWithInstructor() {
        return courseRepository.findAllCoursesWithInstructor()
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
