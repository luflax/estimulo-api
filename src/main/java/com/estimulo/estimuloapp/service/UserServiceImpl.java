/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.service;

import com.estimulo.estimuloapp.model.entity.UserEntity;
import com.estimulo.estimuloapp.model.request.RegisterRequest;
import com.estimulo.estimuloapp.model.response.RegisterResponse;
import com.estimulo.estimuloapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /** {@inheritDoc} */
  public RegisterResponse register(RegisterRequest registerRequest) {
    UserEntity build = UserEntity.builder().username(registerRequest.getUsername()).build();

    UserEntity createdUser = userRepository.save(build);

    return RegisterResponse.builder().id(createdUser.getId()).build();
  }
}
