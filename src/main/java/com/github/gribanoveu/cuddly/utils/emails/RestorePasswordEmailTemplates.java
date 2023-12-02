package com.github.gribanoveu.cuddly.utils.emails;

import com.github.gribanoveu.cuddly.constants.EmailMessages;
import com.github.gribanoveu.cuddly.controllers.dtos.data.SimpleEmailObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Evgeny Gribanov
 * @version 28.11.2023
 */
public abstract class RestorePasswordEmailTemplates {
    public static SimpleEmailObject generateOtpEmail(String sendToEmail, String otpCode, Duration otpCodeLifeTime) {
        return new SimpleEmailObject(
                EmailMessages.sendFrom,
                sendToEmail,
                EmailMessages.restorePasswordEmailSubject,
                EmailMessages.restorePasswordTemplateName,
                Map.of(
                        "otpCode", otpCode,
                        "otpCodeLifetime", otpCodeLifeTime.toMinutes(),
                        "email", sendToEmail
                )
        );
    }

    public static SimpleEmailObject passwordChangedEmail(String sendToEmail) {
        return new SimpleEmailObject(
                EmailMessages.sendFrom,
                sendToEmail,
                EmailMessages.passwordChangedEmailSubject,
                EmailMessages.passwordChangedTemplateName,
                Map.of(
                        "email", sendToEmail
                )
        );
    }
}
