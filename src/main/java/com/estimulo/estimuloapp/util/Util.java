/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.util;

import com.estimulo.estimuloapp.exception.InternalServerErrorException;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

@Log4j2
public class Util {

  private static final String SHA_1 = "SHA-1";

  /**
   * Generates an random sha-1 hash
   *
   * @return Random hashed string
   */
  public static String generateSha1HashCode() {
    Random random = new Random();
    long randomLong = System.currentTimeMillis() + random.nextLong();
    return generateSha1HashCode(Long.toString(randomLong));
  }
  /**
   * Generates an sha-1 hash based on a key
   *
   * @param key Key to generate the sha-1 hash
   * @return Hashed string
   */
  public static String generateSha1HashCode(String key) {
    try {
      MessageDigest instance = MessageDigest.getInstance(SHA_1);
      byte[] hashedByteArray = instance.digest(key.getBytes());

      return Base64.getEncoder().encodeToString(hashedByteArray);
    } catch (NoSuchAlgorithmException ex) {
      log.error("Algorithm {} was not found", SHA_1);
      throw new InternalServerErrorException();
    }
  }
}
