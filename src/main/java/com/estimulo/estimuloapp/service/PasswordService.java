/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.model.request.ChangePasswordRequest;
import com.estimulo.estimuloapp.model.request.VerifyRecoveryCodeRequest;

public interface PasswordService {
  /**
   * Generates a password recovery token and send it through email
   *
   * @param emailAddress The user's email address
   */
  void resetPassword(String emailAddress);

  void verifyRecoveryCode(String emailAddress, String recoveryCode);

  void changePassword(ChangePasswordRequest changePasswordRequest);
}
