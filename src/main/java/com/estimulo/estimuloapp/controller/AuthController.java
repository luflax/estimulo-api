/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.controller;

import com.estimulo.estimuloapp.model.request.LoginRequest;
import com.estimulo.estimuloapp.model.request.RegisterRequest;
import com.estimulo.estimuloapp.model.response.BaseResponse;
import com.estimulo.estimuloapp.model.response.LoginResponse;
import com.estimulo.estimuloapp.model.response.RegisterResponse;
import com.estimulo.estimuloapp.service.AuthService;
import com.estimulo.estimuloapp.service.EmailService;
import com.estimulo.estimuloapp.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = "Auth")
@RequestMapping(value = "/v1/auth")
@Log4j2
public class AuthController {

  private final AuthService authService;
  private final UserService userService;
  private final EmailService emailService;

  public AuthController(
      AuthService authService, UserService userService, EmailService emailService) {
    this.authService = authService;
    this.userService = userService;
    this.emailService = emailService;
  }

  @PostMapping("/login")
  public ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody LoginRequest requestBody) {

    log.info("Login in user: {}", requestBody.getEmail());

    LoginResponse loginResponse = authService.login(requestBody);

    BaseResponse<LoginResponse> response =
        BaseResponse.<LoginResponse>builder().response(loginResponse).build();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/register")
  public ResponseEntity<BaseResponse<RegisterResponse>> register(
      @Valid @RequestBody RegisterRequest requestBody) {

    RegisterResponse registerResponse = userService.register(requestBody);

    BaseResponse<RegisterResponse> response =
        BaseResponse.<RegisterResponse>builder().response(registerResponse).build();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/reset-password")
  public ResponseEntity<BaseResponse<Void>> resetPassword(
      @RequestParam("emailAddress") String emailAddress) {

    authService.resetPassword(emailAddress);

    return new ResponseEntity<>(null, HttpStatus.OK);
  }
}
