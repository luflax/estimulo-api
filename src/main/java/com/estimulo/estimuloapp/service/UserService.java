/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.exception.BadRequestException;
import com.estimulo.estimuloapp.model.entity.UserEntity;
import com.estimulo.estimuloapp.model.request.RegisterRequest;
import com.estimulo.estimuloapp.model.request.UserPatchProfileRequest;
import com.estimulo.estimuloapp.model.response.RegisterResponse;

public interface UserService {
  /**
   * Creates a new user with the received data in the database
   *
   * @param registerRequest The register request body
   * @return {@link RegisterResponse} with the created user data
   */
  RegisterResponse register(RegisterRequest registerRequest);

  /**
   * Validates if a email is available for use in database
   *
   * @param email The email address
   * @throws BadRequestException whether the email is already in use
   */
  void validateEmailUniqueness(String email);

  /**
   * Gets the user entity by email
   *
   * @param email The email address
   * @return {@link UserEntity} containing the user data from database
   */
  UserEntity getUser(String email);

  /**
   * Updates the user profile
   *
   * @param accessToken The user access token
   * @param userPatchProfileRequest The patch user request body
   */
  void patchProfile(String accessToken, UserPatchProfileRequest userPatchProfileRequest);
}
