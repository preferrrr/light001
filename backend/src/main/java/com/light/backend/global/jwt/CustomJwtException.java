package com.light.backend.global.jwt;

import com.light.backend.global.exception.code.JwtExceptionCode;
import io.jsonwebtoken.JwtException;
import lombok.Getter;

public class CustomJwtException extends JwtException {

    @Getter
    private String errorCode;

    public CustomJwtException(JwtExceptionCode jwtExceptionCode) {
        super(jwtExceptionCode.getMessage());
        this.errorCode = jwtExceptionCode.getErrorCode();
    }
}
