/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.exception;

import static com.estimulo.estimuloapp.constant.BaseConstants.BAD_REQUEST_MESSAGE;
import static com.estimulo.estimuloapp.exception.ErrorCode.BAD_REQUEST;

public class BadRequestException extends BaseException {
  public BadRequestException() {
    super(BAD_REQUEST.name(), BAD_REQUEST_MESSAGE);
  }

  public BadRequestException(String errorCode, String friendlyMessage){
    super(errorCode, friendlyMessage);
  }
}
