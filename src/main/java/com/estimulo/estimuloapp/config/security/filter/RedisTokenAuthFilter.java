/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.config.security.filter;

import com.estimulo.estimuloapp.exception.UnAuthorizedException;
import com.estimulo.estimuloapp.model.response.BaseResponse;
import com.estimulo.estimuloapp.model.response.ErrorResponse;
import com.estimulo.estimuloapp.service.RedisUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class RedisTokenAuthFilter extends GenericFilterBean {
  public static final String ACCESS_TOKEN_HEADER_NAME = "accessToken";
  public static final String USER_ID = "userId";
  public final RedisUserService redisUserService;
  public final ObjectMapper objectMapper;

  public RedisTokenAuthFilter(RedisUserService redisUserService) {
    this.redisUserService = redisUserService;
    objectMapper = new ObjectMapper();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    String header = httpServletRequest.getHeader(ACCESS_TOKEN_HEADER_NAME);

    String userId;
    try {
      userId = redisUserService.validateAccessToken(header);
    } catch (UnAuthorizedException ex) {
      setUnAuthorizedResponse(httpServletResponse, ex);
      return;
    }
    request.setAttribute(USER_ID, userId);
    chain.doFilter(request, response);
  }

  public void setUnAuthorizedResponse(
      HttpServletResponse httpServletResponse, UnAuthorizedException ex) throws IOException {
    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    ErrorResponse error =
        ErrorResponse.builder().message(ex.getFriendlyMessage()).errorCode(ex.getMessage()).build();
    BaseResponse<Void> baseResponse =
        BaseResponse.<Void>builder().errors(Collections.singletonList(error)).build();
    httpServletResponse.setContentType("application/json");
    httpServletResponse.setCharacterEncoding("UTF-8");
    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(baseResponse));
    httpServletResponse.getWriter().flush();
    httpServletResponse.getWriter().close();
  }
}
