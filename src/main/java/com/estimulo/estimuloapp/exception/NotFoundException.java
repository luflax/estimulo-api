/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.exception;

import static com.estimulo.estimuloapp.constant.BaseConstants.NOT_FOUND_MESSAGE;
import static com.estimulo.estimuloapp.exception.ErrorCode.NOT_FOUND;

public class NotFoundException extends BaseException {
  public NotFoundException() {
    super(NOT_FOUND.name(), NOT_FOUND_MESSAGE);
  }

  public NotFoundException(String errorCode, String friendlyMessage) {
    super(errorCode, friendlyMessage);
  }
}
