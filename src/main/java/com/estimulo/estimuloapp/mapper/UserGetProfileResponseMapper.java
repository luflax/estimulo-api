/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.mapper;

import com.estimulo.estimuloapp.model.entity.AddressEntity;
import com.estimulo.estimuloapp.model.entity.UserEntity;
import com.estimulo.estimuloapp.model.entity.UserPhoneEntity;
import com.estimulo.estimuloapp.model.response.UserGetProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Objects;

@Mapper
public abstract class UserGetProfileResponseMapper {

  @Mapping(target = "phoneNumber", expression = "java(mapPhone(model))")
  @Mapping(target = "address", expression = "java(mapAddress(model))")
  public abstract UserGetProfileResponse map(UserEntity model);

  String mapPhone(UserEntity model) {
    List<UserPhoneEntity> phones = model.getPhones();
    UserPhoneEntity userPhoneEntity = phones.get(0);
    if (Objects.isNull(userPhoneEntity)) {
      return "";
    }

    return userPhoneEntity.getNumber();
  }

  UserGetProfileResponse.Address mapAddress(UserEntity model) {
    List<AddressEntity> addresses = model.getAddresses();
    AddressEntity addressEntity = addresses.get(0);
    if (Objects.isNull(addressEntity)) {
      return null;
    }
    return UserGetProfileResponse.Address.builder()
        .country(addressEntity.getCountry().getCountry())
        .address(addressEntity.getAddress())
        .state(addressEntity.getState().getState())
        .build();
  }
}
