package edu.unimagdalena.lms2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Instructor{
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String email;
    @Column(name = "full_name",nullable = false)
    private String fullName;
    @Column(name = "created_at",nullable = false)
    private Instant createdAt;
    @Column(name = "updated_at",nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Course> courses = new HashSet<>();
    @OneToOne(mappedBy = "instructor")
    private InstructorProfile instructorProfile;
}