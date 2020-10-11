/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.exception;

import static com.estimulo.estimuloapp.constant.BaseConstants.UNAUTHORIZED_MESSAGE;
import static com.estimulo.estimuloapp.exception.ErrorCode.UNAUTHORIZED;

public class UnAuthorizedException extends BaseException {
  public UnAuthorizedException() {
    super(UNAUTHORIZED.name(), UNAUTHORIZED_MESSAGE);
  }

  public UnAuthorizedException(String errorCode, String friendlyMessage) {
    super(errorCode, friendlyMessage);
  }
}
