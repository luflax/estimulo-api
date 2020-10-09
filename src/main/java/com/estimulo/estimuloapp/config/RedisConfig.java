/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.config;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

  @Value("${redis.name}")
  private String redisClientName;

  @Value("${redis.host}")
  private String redisHost;

  @Value("${redis.port}")
  private Integer redisPort;

  @Value("${redis.password}")
  private String redisPassword;

  @Value("${redis.index}")
  private Integer redisIndex;

  @Bean
  public StatefulRedisConnection<String, String> produceRedisClient() {
    return RedisClient.create(
            RedisURI.builder()
                .withHost(redisHost)
                .withDatabase(redisIndex)
                .withPort(redisPort)
                .withPassword(redisPassword)
                .withClientName(redisClientName)
                .build())
        .connect();
  }
}
