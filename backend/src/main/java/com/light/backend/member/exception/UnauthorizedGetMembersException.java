package com.light.backend.member.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.MemberExceptionCode.*;

public class UnauthorizedGetMembersException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public UnauthorizedGetMembersException() {
        super(UNAUTHORIZED_GET_MEMBERS.getMessage());
        this.exceptionCode = UNAUTHORIZED_GET_MEMBERS;
    }
}
