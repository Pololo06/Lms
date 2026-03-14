package edu.unimagdalena.lms2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "full_name",nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String email;
    @Column(name = "created_at",nullable = false) private Instant createdAt;
    @Column(name = "updated_at",nullable = false) private Instant updatedAt;
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY )
    private Set<Enrollment> enrollments;
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY )
    private Set<Assessment> assessments;
}