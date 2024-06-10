package com.light.backend.slot.exception;

import com.light.backend.global.exception.ExceptionCode;
import com.light.backend.global.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SlotExceptionHandler {

    @ExceptionHandler(UnauthorizedCreateSlotException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedCreateSlotException(final UnauthorizedCreateSlotException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[unauthorized create slot exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }

    @ExceptionHandler(NotCreatedByAdminException.class)
    public ResponseEntity<ExceptionResponse> handleNotCreatedByAdminException(final NotCreatedByAdminException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[not created by admin exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }
}
