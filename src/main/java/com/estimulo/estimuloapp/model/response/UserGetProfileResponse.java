/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserGetProfileResponse {
  @ApiModelProperty("User's email address")
  private String email;

  @ApiModelProperty("User's first name")
  private String firstName;

  @ApiModelProperty("User's last name")
  private String lastName;

  @ApiModelProperty("User's phone number")
  private String phoneNumber;

  @ApiModelProperty("User's address information")
  private Address address;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Address {
    @ApiModelProperty("User's address")
    private String address;

    @ApiModelProperty("User's state")
    private String state;

    @ApiModelProperty("User's country")
    private String country;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Password {
    @ApiModelProperty("User's old password")
    @Exclude
    private String oldPassword;

    @ApiModelProperty("User's new password")
    @Exclude
    private String newPassword;
  }
}
