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

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerifyRecoveryCodeRequest {
  @ApiModelProperty("User's email address")
  @Email
  @Size(max = 75)
  @NotBlank
  public String emailAddress;

  @ApiModelProperty("Password recovery code to be verified")
  @NotBlank
  public String recoveryCode;
}
