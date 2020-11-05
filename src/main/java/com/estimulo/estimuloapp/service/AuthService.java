/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.model.request.LoginRequest;
import com.estimulo.estimuloapp.model.response.LoginResponse;

public interface AuthService {

  /**
   * Log in a user
   *
   * @param loginRequest The login request body
   * @return {@link LoginResponse} containing the access token
   */
  LoginResponse login(LoginRequest loginRequest);

  /**
   * Generates a password recovery token and send it through email
   *
   * @param emailAddress The user's email address
   */
  void resetPassword(String emailAddress);
}
