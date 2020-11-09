/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.exception.BadRequestException;
import com.estimulo.estimuloapp.model.entity.UserEntity;

public interface UserService {

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
  UserEntity getUserByEmail(String email);

  /**
   * Gets the user entity by id
   *
   * @param userId The user's id
   * @return {@link UserEntity} containing the user data from database
   */
  UserEntity getUser(String userId);

  /**
   * Saves user entity to the database
   *
   * @param userEntity The user entity
   * @return {@link UserEntity} containing the user data from database
   */
  UserEntity saveUser(UserEntity userEntity);
}
