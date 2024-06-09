package com.light.backend.global.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {

    HttpStatus getHttpStatus();
    String getErrorCode();
    String getMessage();
}
