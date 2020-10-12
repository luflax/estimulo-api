/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String passwordHash;

  private String firstName;
  private String lastName;
  private String email;
  private String role;

  @OneToMany(
      mappedBy = "user",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      orphanRemoval = true)
  private List<UserPhoneEntity> phones;

  @OneToMany(
      mappedBy = "user",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      orphanRemoval = true)
  private List<AddressEntity> addresses;

  public void addAddress(AddressEntity addressEntity) {
    if (Objects.isNull(addresses)) {
      this.addresses = new ArrayList<>();
    }
    this.addresses.add(addressEntity);
    addressEntity.setUser(this);
  }

  public void addPhone(UserPhoneEntity userPhoneEntity) {
    if (Objects.isNull(phones)) {
      this.phones = new ArrayList<>();
    }
    this.phones.add(userPhoneEntity);
    userPhoneEntity.setUser(this);
  }
}
