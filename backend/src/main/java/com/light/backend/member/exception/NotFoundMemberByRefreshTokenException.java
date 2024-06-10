package com.light.backend.member.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.MemberExceptionCode.NOT_FOUND_MEMBER_BY_REFRESH_TOKEN;

public class NotFoundMemberByRefreshTokenException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public NotFoundMemberByRefreshTokenException() {
        super(NOT_FOUND_MEMBER_BY_REFRESH_TOKEN.getMessage());
        this.exceptionCode = NOT_FOUND_MEMBER_BY_REFRESH_TOKEN;
    }

}
