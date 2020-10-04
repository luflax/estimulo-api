/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.model.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseResponse<T> {
  private List<String> errors;
  private T response;
  @Default private String timestamp = Instant.now().toString();
}
