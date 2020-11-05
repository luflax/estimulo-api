/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.controller;

import com.estimulo.estimuloapp.model.request.UserPatchProfileRequest;
import com.estimulo.estimuloapp.model.response.BaseResponse;
import com.estimulo.estimuloapp.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/user-profile")
@Api(tags = "User Profile")
public class UserProfileController {

  private final UserService userService;

  public UserProfileController(UserService userService) {
    this.userService = userService;
  }

  @PatchMapping(headers = {"accessToken"})
  public ResponseEntity<BaseResponse<Void>> patchProfile(
      @Valid @RequestBody UserPatchProfileRequest userPatchProfileRequest,
      HttpServletRequest httpServletRequest) {
    String userId = (String) httpServletRequest.getAttribute("userId");
    userService.patchProfile(userId, userPatchProfileRequest);

    return new ResponseEntity<>(BaseResponse.<Void>builder().build(), HttpStatus.OK);
  }
}
