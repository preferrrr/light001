package com.light.backend.member.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

public class InvalidRefreshTokenException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public InvalidRefreshTokenException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
