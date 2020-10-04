/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.controller;

import com.estimulo.estimuloapp.model.request.LoginRequest;
import com.estimulo.estimuloapp.model.response.BaseResponse;
import com.estimulo.estimuloapp.model.response.LoginResponse;
import com.estimulo.estimuloapp.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Auth")
@RequestMapping(value = "/auth")
@Log4j2
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody LoginRequest requestBody) {

    log.info("Login in user: {}", requestBody.getUsername());

    //userService.register(requestBody);

    BaseResponse<LoginResponse> loginResponse =
        BaseResponse.<LoginResponse>builder()
            .response(LoginResponse.builder().token("tokeeeeeen").build())
            .build();
    return new ResponseEntity<>(loginResponse, HttpStatus.OK);
  }
}
