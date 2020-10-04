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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequest {
  @ApiModelProperty("User's username")
  private String username;

  @ApiModelProperty("User's password")
  @Exclude
  private String password;
}
