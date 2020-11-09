/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.config.security;

import com.estimulo.estimuloapp.config.security.filter.RedisTokenAuthFilter;
import com.estimulo.estimuloapp.service.RedisUserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  public final RedisUserService redisUserService;

  public WebSecurityConfig(RedisUserService redisUserService) {
    this.redisUserService = redisUserService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .addFilterBefore(
            new RedisTokenAuthFilter(redisUserService), UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .anyRequest()
        .permitAll();
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/v1/auth/**");
    web.ignoring().antMatchers("/v1/password/**");
    web.ignoring().mvcMatchers(HttpMethod.OPTIONS, "/**");
    web.ignoring()
        .mvcMatchers(
            "/swagger-ui.html/**",
            "/configuration/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/webjars/**");
  }
}
