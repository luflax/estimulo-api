/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.exception;

import static com.estimulo.estimuloapp.constant.BaseConstants.INTERNAL_SERVER_ERROR_MESSAGE;
import static com.estimulo.estimuloapp.exception.ErrorCode.INTERNAL_SERVER_ERROR;

public class InternalServerErrorException extends BaseException {

  public InternalServerErrorException() {
    super(INTERNAL_SERVER_ERROR.name(), INTERNAL_SERVER_ERROR_MESSAGE);
  }

  public InternalServerErrorException(String errorCode, String friendlyMessage) {
    super(errorCode, friendlyMessage);
  }
}
