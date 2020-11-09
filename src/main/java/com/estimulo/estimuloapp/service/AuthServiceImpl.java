/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.enumeration.Role;
import com.estimulo.estimuloapp.exception.UnAuthorizedException;
import com.estimulo.estimuloapp.model.entity.AddressEntity;
import com.estimulo.estimuloapp.model.entity.CountryEntity;
import com.estimulo.estimuloapp.model.entity.StateEntity;
import com.estimulo.estimuloapp.model.entity.UserEntity;
import com.estimulo.estimuloapp.model.entity.UserPhoneEntity;
import com.estimulo.estimuloapp.model.request.LoginRequest;
import com.estimulo.estimuloapp.model.request.RegisterRequest;
import com.estimulo.estimuloapp.model.response.LoginResponse;
import com.estimulo.estimuloapp.model.response.RegisterResponse;
import com.estimulo.estimuloapp.util.Util;
import org.springframework.stereotype.Service;

import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.PASSWORD_MISMATCH;

@Service
public class AuthServiceImpl implements AuthService {
  private final UserService userService;
  private final RedisUserService redisUserService;

  public AuthServiceImpl(UserService userService, RedisUserService redisUserService) {
    this.userService = userService;
    this.redisUserService = redisUserService;
  }

  /** {@inheritDoc} */
  public LoginResponse login(LoginRequest loginRequest) {
    UserEntity userEntity = userService.getUserByEmail(loginRequest.getEmail());

    String passwordHash = Util.generateSha1HashCode(loginRequest.getPassword());
    if (!userEntity.getPasswordHash().equals(passwordHash)) {
      throw new UnAuthorizedException(PASSWORD_MISMATCH, "The informed password is incorrect");
    }
    String accessToken = redisUserService.retrieveToken(userEntity.getId().toString());
    return LoginResponse.builder().token(accessToken).build();
  }

  /** {@inheritDoc} */
  public RegisterResponse register(RegisterRequest registerRequest) {
    // TODO: add more validations
    userService.validateEmailUniqueness(registerRequest.getEmail());

    AddressEntity addressEntity = buildAddressEntity(registerRequest);
    UserPhoneEntity userPhoneEntity = buildUserPhoneEntity(registerRequest);
    UserEntity userEntity = buildUserEntity(registerRequest, addressEntity, userPhoneEntity);

    UserEntity createdUser = userService.saveUser(userEntity);

    return RegisterResponse.builder().id(createdUser.getId()).build();
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
