/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.model.request.RegisterRequest;
import com.estimulo.estimuloapp.model.response.RegisterResponse;

public interface UserService {
  RegisterResponse register(RegisterRequest registerRequest);
}
