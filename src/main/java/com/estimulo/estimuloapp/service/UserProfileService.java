/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.model.request.UserPatchProfileRequest;
import com.estimulo.estimuloapp.model.response.UserGetProfileResponse;

public interface UserProfileService {

  /**
   * Retrieves an user profile by user id
   *
   * @param userId The user id
   * @return {@link UserGetProfileResponse} containing the user profile data
   */
  UserGetProfileResponse getUserProfile(String userId);

  /**
   * Updates the user profile
   *
   * @param userId The user id
   * @param userPatchProfileRequest The patch user request body
   */
  void patchProfile(String userId, UserPatchProfileRequest userPatchProfileRequest);
}
