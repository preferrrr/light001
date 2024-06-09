package com.light.backend.member.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.MemberExceptionCode.*;

public class InvalidMemberRoleException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public InvalidMemberRoleException() {
        super(INVALID_MEMBER_ROLE.getMessage());
        this.exceptionCode = INVALID_MEMBER_ROLE;
    }
}
