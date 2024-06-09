package com.light.backend.member.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.MemberExceptionCode.EXISTS_ID;

public class ExistsIdException extends RuntimeException {

    @Getter
    private final ExceptionCode exceptionCode;

    public ExistsIdException() {
        super(EXISTS_ID.getMessage());
        this.exceptionCode = EXISTS_ID;
    }
}
