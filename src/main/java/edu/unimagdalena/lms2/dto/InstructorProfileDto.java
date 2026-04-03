package edu.unimagdalena.lms2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class InstructorProfileDto {

    public record InstructorProfileRequest(
            @NotBlank String phone,
            @NotBlank String bio,
            @NotNull UUID instructorId
    ) {}

    public record InstructorProfileResponse(
            UUID id,
            String phone,
            String bio,
            UUID instructorId,
            String instructorFullName
    ) {}
}
