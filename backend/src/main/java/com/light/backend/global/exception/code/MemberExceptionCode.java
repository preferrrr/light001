package com.light.backend.global.exception.code;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum MemberExceptionCode implements ExceptionCode {

    NOT_FOUND_MEMBER_BY_EMAIL(BAD_REQUEST, "M001", "해당 아이디로 가입한 사용자가 존재하지 않습니다."),
    UNAUTHORIZED_CREATE_MEMBER(UNAUTHORIZED, "M002", "사용자를 생성할 권한이 없습니다."),
    EXISTS_ID(CONFLICT, "M003", "이미 존재하는 아이디입니다."),
    NOT_MATCH_PASSWORD(BAD_REQUEST, "M004", "비밀번호가 틀렸습니다."),
    NOT_FOUND_MEMBER_BY_REFRESH_TOKEN(UNAUTHORIZED, "M004", "리프레시 토큰으로 멤버를 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "M005", "잘못된 리프레시 토큰입니다."),
    INVALID_MEMBER_ROLE(BAD_REQUEST, "M006", "잘못된 사용자 권한입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
