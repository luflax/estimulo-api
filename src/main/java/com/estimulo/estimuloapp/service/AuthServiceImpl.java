/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.exception.UnAuthorizedException;
import com.estimulo.estimuloapp.model.entity.UserEntity;
import com.estimulo.estimuloapp.model.request.LoginRequest;
import com.estimulo.estimuloapp.model.response.LoginResponse;
import com.estimulo.estimuloapp.util.Util;
import org.springframework.stereotype.Service;

import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.PASSWORD_MISMATCH;

@Service
public class AuthServiceImpl implements AuthService {
  private final UserService userService;
  private final RedisUserService redisUserService;

  public AuthServiceImpl(UserService userService, RedisUserService redisUserService) {
    this.userService = userService;
    this.redisUserService = redisUserService;
  }

  /** {@inheritDoc} */
  public LoginResponse login(LoginRequest loginRequest) {
    UserEntity userEntity = userService.getUser(loginRequest.getEmail());

    String passwordHash = Util.generateSha1HashCode(loginRequest.getPassword());
    if (!userEntity.getPasswordHash().equals(passwordHash)) {
      throw new UnAuthorizedException(PASSWORD_MISMATCH, "The informed password is incorrect");
    }
    String accessToken = redisUserService.retrieveToken(userEntity.getId().toString());
    return LoginResponse.builder().token(accessToken).build();
  }
}
