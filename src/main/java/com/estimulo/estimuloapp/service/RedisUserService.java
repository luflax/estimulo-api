/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import java.util.Optional;

public interface RedisUserService {

  /**
   * Retrieves an existing and valid token or generate a new one
   *
   * @param userId The user's id
   * @return The user access token
   */
  String retrieveToken(String userId);

  /**
   * Finds an entire user key by the access token
   *
   * @param token The user access token
   * @return Optional with complete user key (userId:token)
   */
  Optional<String> findKeyByToken(String token);

  /**
   * Finds an entire user key by the user id
   *
   * @param email The user's id
   * @return Optional with complete user key (userId:token)
   */
  Optional<String> findKeyByUserId(String email);

  /**
   * Validates if a access token is valid
   *
   * @param accessToken The access token
   * @return user id whether the access token is valid
   */
  String validateAccessToken(String accessToken);
}
