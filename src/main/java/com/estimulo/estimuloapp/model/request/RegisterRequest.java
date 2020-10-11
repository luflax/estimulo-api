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
@GroupSequence({NotBlankValidator.class, SizeValidatorForCharSequence.class, RegisterRequest.class})
public class RegisterRequest {
  @Email
  @Size(max = 75)
  @NotBlank
  @ApiModelProperty("User's email address")
  private String email;

  @Size(min = 8, max = 30)
  @Exclude
  @NotBlank
  @ApiModelProperty("User's password")
  private String password;

  @Size(max = 75)
  @NotBlank
  @ApiModelProperty("User's first name")
  private String firstName;

  @Size(max = 75)
  @NotBlank
  @ApiModelProperty("User's last name")
  private String lastName;

  @Size(max = 20)
  @NotBlank
  @ApiModelProperty("User's phone number")
  private String phoneNumber;

  @NotBlank
  @ApiModelProperty("User's address")
  private String address;

  @NotBlank
  @ApiModelProperty("User's state")
  private String state;

  @NotBlank
  @ApiModelProperty("User's country")
  private String country;
}
