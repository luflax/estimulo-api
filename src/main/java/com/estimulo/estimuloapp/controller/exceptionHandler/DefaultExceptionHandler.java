/*
 * Copyright (c) 2020 Luiz Flávio Freire Santos
 */

package com.estimulo.estimuloapp.controller.exceptionHandler;

import com.estimulo.estimuloapp.exception.BadRequestException;
import com.estimulo.estimuloapp.exception.NotFoundException;
import com.estimulo.estimuloapp.exception.UnAuthorizedException;
import com.estimulo.estimuloapp.model.response.BaseResponse;
import com.estimulo.estimuloapp.model.response.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.estimulo.estimuloapp.constant.BaseConstants.INTERNAL_SERVER_ERROR_MESSAGE;
import static com.estimulo.estimuloapp.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Log4j2
public class DefaultExceptionHandler {

  @ExceptionHandler({Exception.class})
  public ResponseEntity<BaseResponse<Void>> handlerException(Exception exception) {
    log.error("Unexpected error occurred", exception);

    ErrorResponse error =
        ErrorResponse.builder()
            .errorCode(INTERNAL_SERVER_ERROR.name())
            .message(INTERNAL_SERVER_ERROR_MESSAGE)
            .build();

    return new ResponseEntity<>(
        BaseResponse.<Void>builder().errors(Collections.singletonList(error)).build(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({BadRequestException.class})
  public ResponseEntity<BaseResponse<Void>> handlerBadRequestException(
      BadRequestException exception) {
    ErrorResponse error =
        ErrorResponse.builder()
            .errorCode(exception.getMessage())
            .message(exception.getFriendlyMessage())
            .build();

    return new ResponseEntity<>(
        BaseResponse.<Void>builder().errors(Collections.singletonList(error)).build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({UnAuthorizedException.class})
  public ResponseEntity<BaseResponse<Void>> handlerUnAuthorizedException(
      UnAuthorizedException exception) {
    ErrorResponse error =
        ErrorResponse.builder()
            .errorCode(exception.getMessage())
            .message(exception.getFriendlyMessage())
            .build();

    return new ResponseEntity<>(
        BaseResponse.<Void>builder().errors(Collections.singletonList(error)).build(),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({NotFoundException.class})
  public ResponseEntity<BaseResponse<Void>> handlerNotFoundException(NotFoundException exception) {
    ErrorResponse error =
        ErrorResponse.builder()
            .errorCode(exception.getMessage())
            .message(exception.getFriendlyMessage())
            .build();

    return new ResponseEntity<>(
        BaseResponse.<Void>builder().errors(Collections.singletonList(error)).build(),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<BaseResponse<Void>> handlerArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    BindingResult result = exception.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();

    List<ErrorResponse> errorsList = new ArrayList<>();
    fieldErrors.forEach(
        fieldError -> {
          String formattedErrorMessage =
              String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());

          String formattedErrorCode =
              String.format("%s_INVALID_%s", fieldError.getField(), fieldError.getCode())
                  .toUpperCase();

          ErrorResponse error =
              ErrorResponse.builder()
                  .errorCode(formattedErrorCode)
                  .message(formattedErrorMessage)
                  .build();
          errorsList.add(error);
        });

    return new ResponseEntity<>(
        BaseResponse.<Void>builder().errors(errorsList).build(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({MissingServletRequestParameterException.class})
  public ResponseEntity<BaseResponse<Void>> handlerMissingServletRequestParameterException(
      MissingServletRequestParameterException exception) {

    String formattedErrorMessage =
        String.format("%s: %s", exception.getParameterName(), "must be informed");

    String formattedErrorCode =
        String.format("%s_INVALID_%s", exception.getParameterName(), "NOT_INFORMED")
            .toUpperCase();

    ErrorResponse error =
        ErrorResponse.builder()
            .errorCode(formattedErrorCode)
            .message(formattedErrorMessage)
            .build();

    return new ResponseEntity<>(
        BaseResponse.<Void>builder().errors(Collections.singletonList(error)).build(),
        HttpStatus.BAD_REQUEST);
  }
}
