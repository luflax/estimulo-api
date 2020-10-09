/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseException extends RuntimeException {
  private String friendlyMessage;

  public BaseException(String errorCode, String friendlyMessage) {
    super(errorCode);
    this.friendlyMessage = friendlyMessage;
  }
}
