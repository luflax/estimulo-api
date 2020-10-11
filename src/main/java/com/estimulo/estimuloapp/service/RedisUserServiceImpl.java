/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.exception.InternalServerErrorException;
import com.estimulo.estimuloapp.model.redis.RedisUserModel;
import com.estimulo.estimuloapp.repository.RedisUserRepository;
import com.estimulo.estimuloapp.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class RedisUserServiceImpl implements RedisUserService {

  private final RedisUserRepository redisUserRepository;
  private final Integer timeToLive;

  public RedisUserServiceImpl(
      RedisUserRepository redisUserRepository,
      @Value("${redis.user.time_to_live}") Integer timeToLive) {
    this.redisUserRepository = redisUserRepository;
    this.timeToLive = timeToLive;
  }

  /** {@inheritDoc} */
  public String retrieveToken(String email) {
    Optional<String> keyByEmail = findKeyByEmail(email);

    if (keyByEmail.isPresent()) {
      String key = keyByEmail.get();
      Optional<RedisUserModel> redisUserModelOptional = redisUserRepository.retrieve(key);
      if (!redisUserModelOptional.isPresent()) {
        throw new InternalServerErrorException();
      }

      RedisUserModel redisUserModel = redisUserModelOptional.get();
      // Whether the token's still valid, update ttl and return the same token
      if (redisUserModel.getTimeToLive().after(Timestamp.from(Instant.now()))) {
        redisUserModel.setTimeToLive(
            Timestamp.from(Instant.now().plus(timeToLive, ChronoUnit.MINUTES)));

        String token = key.substring(key.indexOf(":") + 1);
        redisUserRepository.store(email, token, redisUserModel);
        return token;
      }
    }
    return generateNewToken(email);
  }

  /** {@inheritDoc} */
  public Optional<String> findKeyByToken(String token) {
    return redisUserRepository.findKey("*:" + token);
  }

  /** {@inheritDoc} */
  public Optional<String> findKeyByEmail(String email) {
    return redisUserRepository.findKey(email + ":*");
  }

  /**
   * Generates a new token in Redis database based on the user email
   *
   * @param email The user's email address
   * @return String with the access token
   */
  private String generateNewToken(String email) {
    String generatedToken = Util.generateSha1HashCode();
    RedisUserModel redisUserModel =
        RedisUserModel.builder()
            .timeToLive(Timestamp.from(Instant.now().plus(timeToLive, ChronoUnit.MINUTES)))
            .build();
    redisUserRepository.store(email, generatedToken, redisUserModel);
    return generatedToken;
  }
}
