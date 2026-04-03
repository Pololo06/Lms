package edu.unimagdalena.lms2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public class InstructorDto {

    public record InstructorRequest(
            @NotBlank String fullName,
            @NotBlank @Email String email
    ) {}

    public record InstructorResponse(
            UUID id,
            String fullName,
            String email,
            Instant createdAt,
            Instant updatedAt
    ) {}
}