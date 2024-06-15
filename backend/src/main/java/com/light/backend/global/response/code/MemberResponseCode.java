package com.light.backend.global.response.code;

import com.light.backend.global.response.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum MemberResponseCode implements SuccessCode {

    SIGNUP(CREATED, "M01","회원가입에 성공했습니다."),
    LOGIN(OK, "M02","로그인에 성공했습니다."),
    REISSUE_JWT(OK, "M03","JWT가 재발급되었습니다."),
    GET_MEMBERS(OK, "M04", "회원 조회에 성공했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
