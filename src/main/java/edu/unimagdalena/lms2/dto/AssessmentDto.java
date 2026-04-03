package edu.unimagdalena.lms2.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public class AssessmentDto {
    public record AssessmentRequest(
            @NotBlank String type,
            @Min(value = 0)
            @Max(value = 100) int score,
            @NotNull Instant takenAt,
            @NotNull UUID courseId,
            @NotNull UUID studentId) {}

    public record AssessmentResponse(
            UUID id,
            String type,
            int score,
            Instant takenAt,
            UUID courseId,
            String courseTitle,
            UUID studentId,
            String studentFullName
    ) {}
}
