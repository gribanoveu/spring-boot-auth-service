package com.github.gribanoveu.auth.controllers.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.github.gribanoveu.auth.constants.RegexpFormat.PERMISSION_PATTERN;
import static com.github.gribanoveu.auth.constants.ValidationMessages.*;

/**
 * @author Evgeny Gribanov
 * @version 26.09.2023
 */
public record UpdatePermissionDto(
        @NotBlank(message = NOT_BLANK_EXCEPTION_MESSAGE)
        @Pattern(regexp = PERMISSION_PATTERN, message = PATTERN_EXCEPTION_MESSAGE)
        @Size(max = 30, message = SIZE_EXCEPTION_MESSAGE)
        String permissionName,

        @NotBlank(message = NOT_BLANK_EXCEPTION_MESSAGE)
        @Pattern(regexp = PERMISSION_PATTERN, message = PATTERN_EXCEPTION_MESSAGE)
        @Size(max = 30, message = SIZE_EXCEPTION_MESSAGE)
        String newName
) {}
