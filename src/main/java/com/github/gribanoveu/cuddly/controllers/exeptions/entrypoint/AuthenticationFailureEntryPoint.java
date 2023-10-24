package com.github.gribanoveu.cuddly.controllers.exeptions.entrypoint;

import com.github.gribanoveu.cuddly.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Evgeny Gribanov
 * @version 20.10.2023
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFailureEntryPoint implements AuthenticationFailureHandler {
    private final JsonUtils jsonUtils;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        if (exception instanceof DisabledException) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "User is disabled");
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
        }
    }
}
