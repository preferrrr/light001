package com.light.backend.slot.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.SlotExceptionCode.UNAUTHORIZED_DELETE_SLOT;

public class UnauthorizedDeleteSlotException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public UnauthorizedDeleteSlotException() {
        super(UNAUTHORIZED_DELETE_SLOT.getMessage());
        this.exceptionCode = UNAUTHORIZED_DELETE_SLOT;
    }

}
