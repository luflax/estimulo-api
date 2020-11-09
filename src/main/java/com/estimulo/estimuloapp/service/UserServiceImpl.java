/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.exception.BadRequestException;
import com.estimulo.estimuloapp.model.entity.UserEntity;
import com.estimulo.estimuloapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.EMAIL_ALREADY_REGISTERED;
import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.USER_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /** {@inheritDoc} */
  public void validateEmailUniqueness(String email) {
    Optional<UserEntity> userEntityOptional = userRepository.findUserEntityByEmail(email);
    if (userEntityOptional.isPresent()) {
      throw new BadRequestException(
          EMAIL_ALREADY_REGISTERED, String.format("Email: %s is already in use", email));
    }
  }

  /** {@inheritDoc} */
  public UserEntity getUserByEmail(String email) {
    Optional<UserEntity> userEntity = userRepository.findUserEntityByEmail(email);
    if (!userEntity.isPresent()) {
      throw new BadRequestException(
          USER_NOT_FOUND, String.format("User with email %s was not found", email));
    }
    return userEntity.get();
  }

  /** {@inheritDoc} */
  public UserEntity getUser(String userId) {
    Optional<UserEntity> userEntity = userRepository.findById(Long.valueOf(userId));
    if (!userEntity.isPresent()) {
      throw new BadRequestException(
          USER_NOT_FOUND, String.format("User with id %s was not found", userId));
    }
    return userEntity.get();
  }

  /** {@inheritDoc} */
  public UserEntity saveUser(UserEntity userEntity) {
    return userRepository.save(userEntity);
  }
}
