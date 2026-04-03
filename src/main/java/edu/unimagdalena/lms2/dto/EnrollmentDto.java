package edu.unimagdalena.lms2.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public class EnrollmentDto {


    public record EnrollmentCreateRequest(
            @NotNull UUID studentId,
            @NotNull UUID courseId
    ) {}

    public record EnrollmentUpdateRequest(
            @NotNull String status
    ) {}

    public record EnrollmentResponse(
            UUID id,
            String status,
            Instant enrolledAt,
            UUID studentId,
            String studentFullName,
            UUID courseId,
            String courseTitle
    ) {}
}