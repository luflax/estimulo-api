/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;
import org.hibernate.validator.internal.constraintvalidators.bv.size.SizeValidatorForCharSequence;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@GroupSequence({NotBlankValidator.class, SizeValidatorForCharSequence.class, UserPatchProfileRequest.class})
public class UserPatchProfileRequest {
  @Email
  @Size(max = 75)
  @ApiModelProperty("User's email address")
  private String email;

  @ApiModelProperty("User's password information")
  private Password password;

  @Size(max = 75)
  @ApiModelProperty("User's first name")
  private String firstName;

  @Size(max = 75)
  @ApiModelProperty("User's last name")
  private String lastName;

  @Size(max = 20)
  @ApiModelProperty("User's phone number")
  private String phoneNumber;

  @ApiModelProperty("User's address information")
  private Address address;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Address{
    @ApiModelProperty("User's address")
    @NotBlank
    private String address;

    @ApiModelProperty("User's state")
    @NotBlank
    private String state;

    @ApiModelProperty("User's country")
    @NotBlank
    private String country;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Password{
    @ApiModelProperty("User's old password")
    @NotBlank
    @Size(min = 8, max = 30)
    @Exclude
    private String oldPassword;

    @ApiModelProperty("User's new password")
    @NotBlank
    @Size(min = 8, max = 30)
    @Exclude
    private String newPassword;
  }
}
