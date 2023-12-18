package com.github.gribanoveu.cuddly.controllers.dtos.response.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.gribanoveu.cuddly.entities.enums.StatusLevel;
import com.github.gribanoveu.cuddly.entities.tables.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author Evgeny Gribanov
 * @version 26.09.2023
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Europe/Moscow")
    @Schema(description = "Время выполнения запроса")
    private LocalDateTime timestamp;

    @Schema(description = "Уровень статуса запроса", defaultValue = "INFO")
    private StatusLevel status;

    @Schema(description = "Информация о пользователе")
    private Collection<User> users;

    public static UsersResponse create(HttpStatus status, Collection<User> users) {
        return new UsersResponse(LocalDateTime.now(), StatusLevel.SUCCESS,  users);
    }
}
