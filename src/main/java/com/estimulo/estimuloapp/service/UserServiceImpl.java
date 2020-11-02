/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.enumeration.Role;
import com.estimulo.estimuloapp.exception.BadRequestException;
import com.estimulo.estimuloapp.exception.UnAuthorizedException;
import com.estimulo.estimuloapp.model.entity.AddressEntity;
import com.estimulo.estimuloapp.model.entity.CountryEntity;
import com.estimulo.estimuloapp.model.entity.StateEntity;
import com.estimulo.estimuloapp.model.entity.UserEntity;
import com.estimulo.estimuloapp.model.entity.UserPhoneEntity;
import com.estimulo.estimuloapp.model.request.RegisterRequest;
import com.estimulo.estimuloapp.model.request.UserPatchProfileRequest;
import com.estimulo.estimuloapp.model.response.RegisterResponse;
import com.estimulo.estimuloapp.repository.UserRepository;
import com.estimulo.estimuloapp.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.EMAIL_ALREADY_REGISTERED;
import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.PASSWORD_MISMATCH;
import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.USER_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final RedisUserService redisUserService;

  public UserServiceImpl(UserRepository userRepository, RedisUserService redisUserService) {
    this.userRepository = userRepository;
    this.redisUserService = redisUserService;
  }

  /** {@inheritDoc} */
  public RegisterResponse register(RegisterRequest registerRequest) {
    // TODO: add more validations
    validateEmailUniqueness(registerRequest.getEmail());

    AddressEntity addressEntity = buildAddressEntity(registerRequest);
    UserPhoneEntity userPhoneEntity = buildUserPhoneEntity(registerRequest);
    UserEntity userEntity = buildUserEntity(registerRequest, addressEntity, userPhoneEntity);

    UserEntity createdUser = userRepository.save(userEntity);

    return RegisterResponse.builder().id(createdUser.getId()).build();
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
  public UserEntity getUser(String email) {
    Optional<UserEntity> userEntity = userRepository.findUserEntityByEmail(email);
    if (!userEntity.isPresent()) {
      throw new BadRequestException(
          USER_NOT_FOUND, String.format("User with email %s was not found", email));
    }
    return userEntity.get();
  }

  /** {@inheritDoc} */
  @Transactional
  public void patchProfile(String userId, UserPatchProfileRequest userPatchProfileRequest) {

    // Retrieve user existing entity in database
    Optional<UserEntity> userEntityOptional = userRepository.findById(Long.valueOf(userId));
    if (!userEntityOptional.isPresent()) {
      throw new BadRequestException(
          USER_NOT_FOUND, String.format("User with id %s was not found", userId));
    }

    UserEntity userEntity = userEntityOptional.get();

    // Whether any received values is non null, update the user entity

    // 1. General information
    String oldEmail = userEntity.getEmail();
    userEntity.setEmail(Optional.ofNullable(userPatchProfileRequest.getEmail()).orElse(oldEmail));
    userEntity.setFirstName(
        Optional.ofNullable(userPatchProfileRequest.getFirstName())
            .orElse(userEntity.getFirstName()));
    userEntity.setLastName(
        Optional.ofNullable(userPatchProfileRequest.getLastName())
            .orElse(userEntity.getLastName()));

    // 2. Address information
    if (Objects.nonNull(userPatchProfileRequest.getAddress())) {
      AddressEntity addressEntity = buildAddressEntity(userPatchProfileRequest);

      // We're patching the data here, then removing all existing addresses first
      userEntity.getAddresses().clear();
      userEntity.addAddress(addressEntity);
    }

    // 3. Phone number information
    if (Objects.nonNull(userPatchProfileRequest.getPhoneNumber())) {
      UserPhoneEntity userPhoneEntity =
          UserPhoneEntity.builder()
              .number(userPatchProfileRequest.getPhoneNumber())
              .isPrimary(Boolean.TRUE)
              .build();

      // Same here, patching the data, removing all existing phones
      userEntity.getPhones().clear();
      userEntity.addPhone(userPhoneEntity);
    }

    // 4. Password information
    if (Objects.nonNull(userPatchProfileRequest.getPassword())) {
      String oldPasswordHash =
          Util.generateSha1HashCode(userPatchProfileRequest.getPassword().getOldPassword());

      if (!userEntity.getPasswordHash().equals(oldPasswordHash)) {
        throw new UnAuthorizedException(
            PASSWORD_MISMATCH, "The informed old password is incorrect");
      }

      userEntity.setPasswordHash(
          Util.generateSha1HashCode(userPatchProfileRequest.getPassword().getNewPassword()));
    }

    // After modifying the user entity, saves it again
    userRepository.save(userEntity);
  }

  /**
   * Builds an address entity based on the patch profile request
   *
   * @param userPatchProfileRequest The patch user profile request body
   * @return {@link AddressEntity} built with request data
   */
  public AddressEntity buildAddressEntity(UserPatchProfileRequest userPatchProfileRequest) {
    StateEntity stateEntity =
        StateEntity.builder().state(userPatchProfileRequest.getAddress().getState()).build();
    CountryEntity countryEntity =
        CountryEntity.builder().country(userPatchProfileRequest.getAddress().getCountry()).build();
    return AddressEntity.builder()
        .state(stateEntity)
        .address(userPatchProfileRequest.getAddress().getAddress())
        .country(countryEntity)
        .build();
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
            .email(registerRequest.getEmail())
            .firstName(registerRequest.getFirstName())
            .lastName(registerRequest.getLastName())
            .passwordHash(passwordHash)
            .role(Role.USER.name())
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
