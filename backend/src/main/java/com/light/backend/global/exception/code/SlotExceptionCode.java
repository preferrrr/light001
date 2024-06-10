package com.light.backend.global.exception.code;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum SlotExceptionCode implements ExceptionCode {

    UNAUTHORIZED_CREATE_SLOT(UNAUTHORIZED, "S001", "슬롯을 생성할 권한이 없습니다."),
    NOT_CREATED_BY_ADMIN(UNAUTHORIZED, "S002", "담당자가 아닙니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
