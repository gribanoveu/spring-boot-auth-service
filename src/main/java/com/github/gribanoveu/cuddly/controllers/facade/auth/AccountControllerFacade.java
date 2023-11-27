package com.github.gribanoveu.cuddly.controllers.facade.auth;

import com.github.gribanoveu.cuddly.controllers.dtos.request.auth.ChangeEmailDto;
import com.github.gribanoveu.cuddly.controllers.dtos.request.auth.ChangePasswordDto;
import com.github.gribanoveu.cuddly.controllers.dtos.request.auth.GenerateOtpDto;
import com.github.gribanoveu.cuddly.controllers.dtos.request.auth.RestorePasswordDto;
import com.github.gribanoveu.cuddly.controllers.dtos.response.StatusResponse;
import com.github.gribanoveu.cuddly.controllers.exeptions.CredentialEx;
import com.github.gribanoveu.cuddly.entities.enums.ResponseCode;
import com.github.gribanoveu.cuddly.entities.enums.StatusLevel;
import com.github.gribanoveu.cuddly.entities.services.otp.RedisOtpService;
import com.github.gribanoveu.cuddly.entities.services.user.UserService;
import com.github.gribanoveu.cuddly.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author Evgeny Gribanov
 * @version 22.09.2023
 */
@Service
@RequiredArgsConstructor
public class AccountControllerFacade {
    @Value("${time-variable.otpCodeLifetime}")
    private Duration otpCodeLifeTime;
    private final UserService userService;
    private final RedisOtpService redisOtpService;
    private final PasswordEncoder passwordEncoder;
    private final JsonUtils jsonUtils;

    // user change email from app, authenticated
    public ResponseEntity<StatusResponse> changeEmail(ChangeEmailDto request, Authentication authentication) {
        var user = userService.findUserByEmail(authentication.getName());
        userService.updateEmail(user, request.email());
        return ResponseEntity.ok(StatusResponse.create(ResponseCode.USER_UPDATED, StatusLevel.SUCCESS));
    }

    // check that password and confirm password match
    // find user, if not exist -> error
    // verify that the old password matches, if not -> error
    // check that old password not equals new password, else -> error
    // change password
    public ResponseEntity<StatusResponse> changePassword(ChangePasswordDto request, Authentication authentication) {
        if (!request.password().equals(request.confirmPassword())) throw new CredentialEx(ResponseCode.PASSWORD_NOT_EQUALS);

        var user = userService.findUserByEmail(authentication.getName());
        if (passwordEncoder.matches(request.oldPassword(), user.getPassword()))
            if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                userService.updatePasswordByEmail(user, passwordEncoder.encode(request.password()));
                return ResponseEntity.ok(StatusResponse.create(ResponseCode.PASSWORD_UPDATED, StatusLevel.SUCCESS));
            } else throw new CredentialEx(ResponseCode.PASSWORD_EQUALS);

        throw new CredentialEx(ResponseCode.OLD_PASSWORD_NOT_MATCH);
    }

    // user open restore form and enter email
    // service find user by email,
    // if user not found -> error
    // service find otp code
    // if exist -> error
    // else create new code
    // save otp code to db (with lifetime)
    // todo send email with code
    public ResponseEntity<StatusResponse> generateOtpCode(GenerateOtpDto request) {
        var userExist = userService.userExistByEmail(request.email());
        if (!userExist) throw new CredentialEx(ResponseCode.USER_NOT_EXIST);
        var otpCode = jsonUtils.generateRandomOtpCode().toString();
        redisOtpService.saveOptCode(request.email(), otpCode, otpCodeLifeTime);
        return ResponseEntity.ok(StatusResponse.create(ResponseCode.OTP_CODE_CREATED, StatusLevel.SUCCESS));
    }

    // next screen user enter otp code and new password and re-enter password again
    // service check that code exist and have valid lifetime
    // service find userId by otp code and change password in db
    // send successful email
    public ResponseEntity<StatusResponse> restorePasswordByOtp(RestorePasswordDto request) {
        if (!request.password().equals(request.confirmPassword())) throw new CredentialEx(ResponseCode.PASSWORD_NOT_EQUALS);
        if (!redisOtpService.otpCodeValid(request.email(), request.otpCode())) throw new CredentialEx(ResponseCode.OTP_CODE_NOT_FOUND);

        var user = userService.findUserByEmail(request.email());
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            userService.updatePasswordByEmail(user, passwordEncoder.encode(request.password()));
            redisOtpService.deleteOtpCode(request.email());
            return ResponseEntity.ok(StatusResponse.create(ResponseCode.PASSWORD_UPDATED, StatusLevel.SUCCESS));
        }
        throw new CredentialEx(ResponseCode.PASSWORD_EQUALS);
    }
}
