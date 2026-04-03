package edu.unimagdalena.lms2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class LessonDto {
    public record LessonCreateRequest(
            @NotBlank String title,
            @NotNull int orderIndex,
            @NotNull UUID courseId
    ) {}

    public record LessonResponse(
            UUID id,
            String title,
            int orderIndex,
            UUID courseId,
            String courseTitle
    ) {}
}

