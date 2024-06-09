package com.light.backend.global.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponse {

    private String message;
    private String errorCode;

    @Builder
    private ExceptionResponse(String errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }


    public static ExceptionResponse of(ExceptionCode exceptionCode) {
        return ExceptionResponse.builder()
                .errorCode(exceptionCode.getErrorCode())
                .message(exceptionCode.getMessage())
                .build();
    }

    public static ExceptionResponse of(ExceptionCode exceptionCode, String message) {
        return ExceptionResponse.builder()
                .errorCode(exceptionCode.getErrorCode())
                .message(message)
                .build();
    }
}