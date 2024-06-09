package com.light.backend.global.exception.code;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum MemberExceptionCode implements ExceptionCode {

    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
