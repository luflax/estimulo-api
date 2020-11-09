/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.exception.UnAuthorizedException;
import com.estimulo.estimuloapp.mapper.UserGetProfileResponseMapper;
import com.estimulo.estimuloapp.model.entity.AddressEntity;
import com.estimulo.estimuloapp.model.entity.CountryEntity;
import com.estimulo.estimuloapp.model.entity.StateEntity;
import com.estimulo.estimuloapp.model.entity.UserEntity;
import com.estimulo.estimuloapp.model.entity.UserPhoneEntity;
import com.estimulo.estimuloapp.model.request.UserPatchProfileRequest;
import com.estimulo.estimuloapp.model.response.UserGetProfileResponse;
import com.estimulo.estimuloapp.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.PASSWORD_MISMATCH;

@Service
public class UserProfileServiceImpl implements UserProfileService {

  private final UserService userService;
  private UserGetProfileResponseMapper userGetProfileResponseMapper;

  public UserProfileServiceImpl(
      UserService userService, UserGetProfileResponseMapper userGetProfileResponseMapper) {
    this.userService = userService;
    this.userGetProfileResponseMapper = userGetProfileResponseMapper;
  }

  /** {@inheritDoc} */
  @Transactional
  public void patchProfile(String userId, UserPatchProfileRequest userPatchProfileRequest) {

    // Retrieve user existing entity in database
    UserEntity userEntity = userService.getUser(userId);

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
    userService.saveUser(userEntity);
  }

  /** {@inheritDoc} */
  @Transactional
  public UserGetProfileResponse getUserProfile(String userId) {
    UserEntity user = userService.getUser(userId);
    return userGetProfileResponseMapper.map(user);
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
}
