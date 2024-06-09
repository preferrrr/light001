package com.light.backend.member.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.MemberExceptionCode.*;

public class UnauthorizedCreateMemberException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public UnauthorizedCreateMemberException() {
        super(UNAUTHORIZED_CREATE_MEMBER.getMessage());
        this.exceptionCode = UNAUTHORIZED_CREATE_MEMBER;
    }
}
