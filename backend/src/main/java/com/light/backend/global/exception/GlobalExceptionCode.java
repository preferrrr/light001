package com.light.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalExceptionCode implements ExceptionCode {
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
