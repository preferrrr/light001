package com.light.backend.global.exception.code;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionCode implements ExceptionCode {
    EXPIRED(HttpStatus.UNAUTHORIZED, "J001", "토큰이 만료되었습니다."),
    UNSUPPORTED(HttpStatus.UNAUTHORIZED, "J002", "지원하지 않는 형식입니다."),
    MALFORMED(HttpStatus.UNAUTHORIZED, "J003", "잘못된 형식입니다."),
    SIGNATURE(HttpStatus.UNAUTHORIZED, "J004", "잘못된 서명입니다."),
    ILLEGAL_ARGUMENT(HttpStatus.UNAUTHORIZED, "J005", "토큰이 null 또는 공백입니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "J006", "잘못된 토큰입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
