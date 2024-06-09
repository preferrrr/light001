package com.light.backend.member.exception;

import com.light.backend.global.exception.ExceptionCode;
import com.light.backend.global.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class MemberExceptionHandler {

    @ExceptionHandler(ExistsIdException.class)
    public ResponseEntity<ExceptionResponse> handleExistsIdException(final ExistsIdException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[exists id exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }

    @ExceptionHandler(UnauthorizedCreateMemberException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedCreateMemberException(final UnauthorizedCreateMemberException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[unauthorized create member exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundMemberException(final NotFoundMemberException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[not found member by email exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }

    @ExceptionHandler(InvalidMemberRoleException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidMemberRoleException(final InvalidMemberRoleException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[invalid member role exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleNotMatchPasswordException(final NotMatchPasswordException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[not match password exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }


}
