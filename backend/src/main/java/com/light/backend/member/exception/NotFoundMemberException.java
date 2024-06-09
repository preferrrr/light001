package com.light.backend.member.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.MemberExceptionCode.NOT_FOUND_MEMBER_BY_EMAIL;

public class NotFoundMemberException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public NotFoundMemberException() {
        super(NOT_FOUND_MEMBER_BY_EMAIL.getMessage());
        this.exceptionCode = NOT_FOUND_MEMBER_BY_EMAIL;
    }
}
