package com.light.backend.slot.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.SlotExceptionCode.*;

public class UnauthorizedUpdatePaymentStateException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public UnauthorizedUpdatePaymentStateException() {
        super(UNAUTHORIZED_UPDATE_PAYMENT.getMessage());
        this.exceptionCode = UNAUTHORIZED_UPDATE_PAYMENT;
    }
}
