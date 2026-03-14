package edu.unimagdalena.lms2.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "instructor_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstructorProfile {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String bio;
    @OneToOne(optional = false)
    @JoinColumn(name = "instructor_id", referencedColumnName = "id")
    private Instructor instructor;
}