/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.repository;

import com.estimulo.estimuloapp.exception.InternalServerErrorException;
import com.estimulo.estimuloapp.model.redis.RedisUserModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.estimulo.estimuloapp.constant.BaseConstants.INTERNAL_SERVER_ERROR_MESSAGE;
import static com.estimulo.estimuloapp.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@Component
@Log4j2
public class RedisUserRepository {
  private final StatefulRedisConnection<String, String> redisConnection;
  private final ObjectMapper objectMapper;

  public RedisUserRepository(
      StatefulRedisConnection<String, String> redisConnection, ObjectMapper objectMapper) {
    this.redisConnection = redisConnection;
    this.objectMapper = objectMapper;
  }

  /**
   * Creates or updates a redis user model entry into Redis database
   *
   * @param key The redis user key
   * @param redisUserModel The user's data model
   */
  public void store(String key, RedisUserModel redisUserModel) {
    String redisUserModelAsString;
    try {
      redisUserModelAsString = objectMapper.writeValueAsString(redisUserModel);
    } catch (JsonProcessingException ex) {
      log.error("Error processing object to json", ex);
      throw new InternalServerErrorException(
          INTERNAL_SERVER_ERROR.name(), INTERNAL_SERVER_ERROR_MESSAGE);
    }
    redisConnection.sync().set(key, redisUserModelAsString);
  }

  public void erase(String key){
    redisConnection.sync().del(key);
  }

  public Optional<String> findKey(String pattern) {
    List<String> keys = redisConnection.sync().keys(pattern);
    if (Objects.isNull(keys) || keys.isEmpty()) {
      return Optional.empty();
    }
    String foundKey = keys.stream().findFirst().orElseThrow(InternalServerErrorException::new);

    return Optional.of(foundKey);
  }

  public Optional<RedisUserModel> retrieve(String key) {
    String redisUserModelAsString = redisConnection.sync().get(key);

    if (Objects.isNull(redisUserModelAsString)) {
      return Optional.empty();
    }

    try {
      return Optional.of(objectMapper.readValue(redisUserModelAsString, RedisUserModel.class));
    } catch (JsonProcessingException ex) {
      log.error("Error processing object to json", ex);
      throw new InternalServerErrorException(
          INTERNAL_SERVER_ERROR.name(), INTERNAL_SERVER_ERROR_MESSAGE);
    }
  }
}
