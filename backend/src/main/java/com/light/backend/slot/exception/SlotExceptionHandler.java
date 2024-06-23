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

    @ExceptionHandler(NotFoundSlotException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundSlotException(final NotFoundSlotException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[not found slot exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }

    @ExceptionHandler(UnauthorizedSetSlotDataException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedSetSlotDataException(final UnauthorizedSetSlotDataException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[unauthorized set slot data exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }

    @ExceptionHandler(InvalidQueryStringForGetSlotsException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidQueryStringForGetSlotsException(final InvalidQueryStringForGetSlotsException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[invalid query string for get slots exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }

    @ExceptionHandler(UnauthorizedDeleteSlotException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedDeleteSlotException(final UnauthorizedDeleteSlotException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[unauthorized delete slot exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }

    @ExceptionHandler(UnauthorizedUpdatePaymentStateException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedUpdatePaymentStateException(final UnauthorizedUpdatePaymentStateException e) {

        ExceptionCode exceptionCode = e.getExceptionCode();
        log.error("[unauthorized update payment exception] {}", e.getMessage());

        return new ResponseEntity<>(
                ExceptionResponse.of(exceptionCode),
                exceptionCode.getHttpStatus()
        );
    }
}
