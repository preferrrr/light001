package com.light.backend.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.light.backend.global.exception.GlobalExceptionCode.*;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handleBindException(final BindException e) {

        String errorMessage = e.getAllErrors().get(0).getDefaultMessage();
        log.error("[bind exception] {}", errorMessage);

        return new ResponseEntity<>(
                ExceptionResponse.of(INVALID_REQUEST_PARAMETER, errorMessage),
                INVALID_REQUEST_PARAMETER.getHttpStatus()
        );
    }
}
