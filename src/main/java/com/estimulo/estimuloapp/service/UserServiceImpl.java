/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.exception.BadRequestException;
import com.estimulo.estimuloapp.model.entity.AddressEntity;
import com.estimulo.estimuloapp.model.entity.CountryEntity;
import com.estimulo.estimuloapp.model.entity.StateEntity;
import com.estimulo.estimuloapp.model.entity.UserEntity;
import com.estimulo.estimuloapp.model.entity.UserPhoneEntity;
import com.estimulo.estimuloapp.model.request.RegisterRequest;
import com.estimulo.estimuloapp.model.response.RegisterResponse;
import com.estimulo.estimuloapp.repository.UserRepository;
import com.estimulo.estimuloapp.util.Util;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.EMAIL_ALREADY_REGISTERED;
import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.USERNAME_ALREADY_REGISTERED;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /** {@inheritDoc} */
  public RegisterResponse register(RegisterRequest registerRequest) {
    // TODO: add more validations
    validateUsernameUniqueness(registerRequest.getUsername());
    validateEmailUniqueness(registerRequest.getEmail());

    AddressEntity addressEntity = buildAddressEntity(registerRequest);
    UserPhoneEntity userPhoneEntity = buildUserPhoneEntity(registerRequest);
    UserEntity userEntity = buildUserEntity(registerRequest, addressEntity, userPhoneEntity);

    UserEntity createdUser = userRepository.save(userEntity);

    return RegisterResponse.builder().id(createdUser.getId()).build();
  }

  /** {@inheritDoc} */
  public void validateUsernameUniqueness(String username) {
    Optional<UserEntity> userEntityOptional = userRepository.findUserEntityByUsername(username);
    if (userEntityOptional.isPresent()) {
      throw new BadRequestException(
          USERNAME_ALREADY_REGISTERED, String.format("Username: %s is already in use", username));
    }
  }

  /** {@inheritDoc} */
  public void validateEmailUniqueness(String email) {
    Optional<UserEntity> userEntityOptional = userRepository.findUserEntityByEmail(email);
    if (userEntityOptional.isPresent()) {
      throw new BadRequestException(
          EMAIL_ALREADY_REGISTERED, String.format("Email: %s is already in use", email));
    }
  }

  /**
   * Builds the user entity with the request properties
   *
   * @param registerRequest The request body
   * @param addressEntity The address entity created with request properties
   * @param userPhoneEntity The user phone entity created with request properties
   * @return {@link UserEntity} populated with received properties
   */
  private UserEntity buildUserEntity(
      RegisterRequest registerRequest,
      AddressEntity addressEntity,
      UserPhoneEntity userPhoneEntity) {
    String passwordHash = Util.generateSha1HashCode(registerRequest.getPassword());

    UserEntity userEntity =
        UserEntity.builder()
            .username(registerRequest.getUsername())
            .email(registerRequest.getEmail())
            .firstName(registerRequest.getFirstName())
            .lastName(registerRequest.getLastName())
            .passwordHash(passwordHash)
            .build();

    userEntity.addAddress(addressEntity);
    userEntity.addPhone(userPhoneEntity);
    return userEntity;
  }

  /**
   * Builds the user phone entity with the request properties
   *
   * @param registerRequest The request body
   * @return {@link UserPhoneEntity} populated with received properties
   */
  private UserPhoneEntity buildUserPhoneEntity(RegisterRequest registerRequest) {
    return UserPhoneEntity.builder()
        .isPrimary(Boolean.TRUE)
        .number(registerRequest.getPhoneNumber())
        .build();
  }

  /**
   * Builds the address entity with the request properties
   *
   * @param registerRequest The request body
   * @return {@link AddressEntity} populated with received properties
   */
  private AddressEntity buildAddressEntity(RegisterRequest registerRequest) {
    CountryEntity countryEntity =
        CountryEntity.builder().country(registerRequest.getCountry()).build();

    StateEntity stateEntity = StateEntity.builder().state(registerRequest.getState()).build();

    return AddressEntity.builder()
        .address(registerRequest.getAddress())
        .country(countryEntity)
        .state(stateEntity)
        .build();
  }
}
