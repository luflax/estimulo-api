/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.controller;

import com.estimulo.estimuloapp.model.request.ChangePasswordRequest;
import com.estimulo.estimuloapp.model.response.BaseResponse;
import com.estimulo.estimuloapp.service.PasswordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RestController
@Api(tags = "Password")
@RequestMapping("/v1/password")
@Log4j2
public class PasswordController {
  private final PasswordService passwordService;

  public PasswordController(PasswordService passwordService) {
    this.passwordService = passwordService;
  }

  @PostMapping("/reset-password")
  public ResponseEntity<BaseResponse<Void>> resetPassword(
      @RequestParam("emailAddress") @NotBlank String emailAddress) {

    passwordService.resetPassword(emailAddress);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/verify-recovery-code")
  public ResponseEntity<BaseResponse<Void>> verifyRecoveryCode(
      @ApiParam("User's email address")
          @Email
          @Size(max = 75)
          @NotBlank
          @RequestParam("emailAddress")
          String emailAddress,
      @ApiParam("Password recovery code to be verified") @NotBlank @RequestParam("recoveryCode")
          String recoveryCode) {

    passwordService.verifyRecoveryCode(emailAddress, recoveryCode);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/change-password")
  public ResponseEntity<BaseResponse<Void>> changePassword(
      @RequestBody @Valid ChangePasswordRequest changePasswordRequest) {

    passwordService.changePassword(changePasswordRequest);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
