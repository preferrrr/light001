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
    NOT_FOUND_SLOT(NOT_FOUND, "S003", "슬롯을 찾을 수 없습니다."),
    UNAUTHORIZED_SET_SLOT_DATA(UNAUTHORIZED, "S004", "슬롯 데이터를 입력할 권한이 없습니다."),
    INVALID_QUERY_STRING_FOR_GET_SLOTS(BAD_REQUEST, "S005", "잘못된 슬롯 조회 쿼리 스트링 입니다."),
    UNAUTHORIZED_DELETE_SLOT(UNAUTHORIZED, "S006", "슬롯을 삭제할 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
