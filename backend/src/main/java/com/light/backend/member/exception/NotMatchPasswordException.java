package com.light.backend.member.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.MemberExceptionCode.NOT_MATCH_PASSWORD;

public class NotMatchPasswordException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public NotMatchPasswordException() {
        super(NOT_MATCH_PASSWORD.getMessage());
        this.exceptionCode = NOT_MATCH_PASSWORD;
    }
}
