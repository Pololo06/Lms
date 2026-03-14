package edu.unimagdalena.lms2.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assessments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Assessment {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private int score;
    @Column(name = "taken_at",nullable = false) private Instant takenAt;
    @ManyToOne()
    @JoinColumn(name = "course_id")
    private Course course;
    @ManyToOne()
    @JoinColumn(name = "student_id")
    private Student student;
}