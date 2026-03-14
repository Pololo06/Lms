package edu.unimagdalena.lms2.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lesson {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Column(name = "order_index", nullable = false)
    private int orderIndex;
    @ManyToOne()
    @JoinColumn(name = "course_id")
    private Course course;
}