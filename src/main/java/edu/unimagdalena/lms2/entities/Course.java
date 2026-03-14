package edu.unimagdalena.lms2.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "courses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne()
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private Boolean active;
    @Column(name = "created_at",nullable = false)
    private Instant createdAt;
    @Column(name = "updated_at",nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<Enrollment> enrollments;
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<Assessment> assessments;
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<Lesson> lessons;

}