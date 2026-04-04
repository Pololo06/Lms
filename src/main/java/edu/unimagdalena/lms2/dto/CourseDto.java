package edu.unimagdalena.lms2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public class CourseDto {

    public record CourseRequest(
            @NotBlank
            @Size(min = 3, max = 100)
            String title,
            @NotNull String status,
            @NotNull UUID instructorId,
            @NotNull Boolean active
    ) {}

    public record CourseResponse(
            UUID id,
            String title,
            String status,
            Boolean active,
            Instant createdAt,
            Instant updatedAt,
            UUID instructorId,
            String instructorFullName
    ) {}
}