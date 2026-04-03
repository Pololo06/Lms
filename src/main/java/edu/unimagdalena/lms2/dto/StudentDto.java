package edu.unimagdalena.lms2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public class StudentDto {
    public record StudentRequest (
            @NotBlank String fullName,
            @NotBlank String email,
            @NotNull Instant createdAt,
            @NotNull Instant updatedAt
    ) {}

    public record StudentResponse(
            UUID id,
            String fullName,
            String email,
            Instant createdAt,
            Instant updatedAt
    ) {}
}
