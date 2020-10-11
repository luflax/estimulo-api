/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import java.util.Optional;

public interface RedisUserService {

  /**
   * Retrieves an existing and valid token or generate a new one
   *
   * @param email The user's email address
   * @return The user access token
   */
  String retrieveToken(String email);

  /**
   * Finds an entire user key by the access token
   *
   * @param token The user access token
   * @return Optional with complete user key (email:token)
   */
  Optional<String> findKeyByToken(String token);

  /**
   * Finds an entire user key by the email address
   *
   * @param email The user email address
   * @return Optional with complete user key (email:token)
   */
  Optional<String> findKeyByEmail(String email);
}
