/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.exception.InternalServerErrorException;
import com.estimulo.estimuloapp.exception.NotFoundException;
import com.estimulo.estimuloapp.exception.UnAuthorizedException;
import com.estimulo.estimuloapp.model.redis.RedisUserModel;
import com.estimulo.estimuloapp.repository.RedisUserRepository;
import com.estimulo.estimuloapp.util.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.INVALID_TOKEN;
import static com.estimulo.estimuloapp.exception.ApplicationErrorCode.PASSWORD_MISMATCH;

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
  public String retrieveToken(String userId) {
    Optional<String> keyByUserId = findKeyByUserId(userId);

    if (keyByUserId.isPresent()) {
      String key = keyByUserId.get();
      Optional<RedisUserModel> redisUserModelOptional = redisUserRepository.retrieve(key);
      if (!redisUserModelOptional.isPresent()) {
        throw new InternalServerErrorException();
      }

      RedisUserModel redisUserModel = redisUserModelOptional.get();
      // Whether the token's still valid, update ttl and return the same token
      if (redisUserModel.getTimeToLive().after(Timestamp.from(Instant.now()))) {
        redisUserModel.setTimeToLive(
            Timestamp.from(Instant.now().plus(timeToLive, ChronoUnit.MINUTES)));

        String token = extractTokenFromKey(key);
        redisUserRepository.store(String.format("%s:%s", userId, token), redisUserModel);
        return token;
      }
    }
    return generateNewToken(userId);
  }

  /** {@inheritDoc} */
  public Optional<String> findKeyByToken(String token) {
    return redisUserRepository.findKey("*:" + token);
  }

  /** {@inheritDoc} */
  public Optional<String> findKeyByUserId(String userId) {
    return redisUserRepository.findKey(userId + ":*");
  }

  /** {@inheritDoc} */
  public String validateAccessToken(String accessToken) {
    Optional<String> keyByToken = findKeyByToken(accessToken);
    if(!keyByToken.isPresent()){
      throw new UnAuthorizedException(INVALID_TOKEN, "The received access token is not valid");
    }

    return extractUserIdFromKey(keyByToken.get());
  }

  private String extractUserIdFromKey(String key) {
    return key.substring(0, key.indexOf(":"));
  }

  private String extractTokenFromKey(String key) {
    return key.substring(key.indexOf(":") + 1);
  }

  /**
   * Generates a new entry in Redis database for the user id
   *
   * @param userId The user's id
   * @return String with the access token
   */
  private String generateNewToken(String userId) {
    String generatedToken = Util.generateSha1HashCode();
    RedisUserModel redisUserModel =
        RedisUserModel.builder()
            .timeToLive(Timestamp.from(Instant.now().plus(timeToLive, ChronoUnit.MINUTES)))
            .build();
    redisUserRepository.store(String.format("%s:%s", userId, generatedToken), redisUserModel);
    return generatedToken;
  }
}
