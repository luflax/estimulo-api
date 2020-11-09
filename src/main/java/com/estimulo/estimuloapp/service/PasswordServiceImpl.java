/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.exception.BadRequestException;
import com.estimulo.estimuloapp.model.entity.UserEntity;
import com.estimulo.estimuloapp.model.request.ChangePasswordRequest;
import com.estimulo.estimuloapp.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.EXPIRED_RECOVERY_CODE;
import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.INVALID_RECOVERY_CODE;

@Service
public class PasswordServiceImpl implements PasswordService {

  private final UserService userService;
  private final EmailService emailService;
  private final Integer recoveryCodeMinutes;

  public PasswordServiceImpl(
      UserService userService,
      EmailService emailService,
      @Value("${recoveryCode.minutes}") Integer recoveryCodeMinutes) {
    this.userService = userService;
    this.emailService = emailService;
    this.recoveryCodeMinutes = recoveryCodeMinutes;
  }

  /** {@inheritDoc} */
  public void resetPassword(String emailAddress) {
    UserEntity userEntity = userService.getUserByEmail(emailAddress);

    String recoveryCode = Util.generateRecoveryCode();
    userEntity.setRecoveryPasswordCode(recoveryCode);
    userEntity.setRecoveryPasswordCodeTime(LocalDateTime.now());
    userService.saveUser(userEntity);

    emailService.sendEmail(
        emailAddress,
        String.format(
            "<h3>Recuperação de senha</h3> <br>Seu código de recuperação de senha é: %s",
            recoveryCode),
        "Seu código de recuperação de senha",
        Boolean.TRUE);
  }

  /** {@inheritDoc} */
  public void verifyRecoveryCode(String emailAddress, String recoveryCode) {
    UserEntity userEntity = userService.getUserByEmail(emailAddress);

    if (Boolean.FALSE.equals(userEntity.getRecoveryPasswordCode().equals(recoveryCode))) {
      throw new BadRequestException(
          INVALID_RECOVERY_CODE, String.format("Invalid recovery code to user: %s", emailAddress));
    }
    if (LocalDateTime.now()
        .isAfter(userEntity.getRecoveryPasswordCodeTime().plusMinutes(recoveryCodeMinutes))) {
      throw new BadRequestException(
          EXPIRED_RECOVERY_CODE, String.format("Expired recovery code to user: %s", emailAddress));
    }
  }

  /** {@inheritDoc} */
  public void changePassword(ChangePasswordRequest changePasswordRequest) {
    verifyRecoveryCode(
        changePasswordRequest.getEmailAddress(), changePasswordRequest.getRecoveryCode());

    UserEntity userEntity = userService.getUserByEmail(changePasswordRequest.getEmailAddress());

    String passwordHash = Util.generateSha1HashCode(changePasswordRequest.getNewPassword());
    userEntity.setPasswordHash(passwordHash);

    userService.saveUser(userEntity);
  }
}
