package com.light.backend.global.response.code;

import com.light.backend.global.response.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberResponseCode implements SuccessCode {

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
