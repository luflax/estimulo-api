/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.model.request.LoginRequest;
import com.estimulo.estimuloapp.model.request.RegisterRequest;
import com.estimulo.estimuloapp.model.response.LoginResponse;
import com.estimulo.estimuloapp.model.response.RegisterResponse;

public interface AuthService {

  /**
   * Log in a user
   *
   * @param loginRequest The login request body
   * @return {@link LoginResponse} containing the access token
   */
  LoginResponse login(LoginRequest loginRequest);

  /**
   * Creates a new user with the received data in the database
   *
   * @param registerRequest The register request body
   * @return {@link RegisterResponse} with the created user data
   */
  RegisterResponse register(RegisterRequest registerRequest);
}
