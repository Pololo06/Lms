package edu.unimagdalena.lms2.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Enrollment {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String status;
    @Column(name = "enrolled_at",nullable = false) private Instant enrolledAt;
    @ManyToOne()
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne()
    @JoinColumn(name = "course_id")
    private Course course;
}