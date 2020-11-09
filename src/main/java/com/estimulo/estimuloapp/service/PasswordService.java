/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.model.request.ChangePasswordRequest;

public interface PasswordService {
  /**
   * Generates a password recovery token and send it through email
   *
   * @param emailAddress The user's email address
   */
  void resetPassword(String emailAddress);

  /**
   * Verifies if a recovery code is valid
   *
   * @param emailAddress The email address that recoveryCode is associated with
   * @param recoveryCode The recovery password code
   */
  void verifyRecoveryCode(String emailAddress, String recoveryCode);

  /**
   * Changes an user password if the recovery token is valid
   *
   * @param changePasswordRequest Model containing the new password, recovery code and email address
   */
  void changePassword(ChangePasswordRequest changePasswordRequest);
}
