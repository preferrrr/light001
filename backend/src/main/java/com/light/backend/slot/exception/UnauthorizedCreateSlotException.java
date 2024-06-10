package com.light.backend.slot.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.SlotExceptionCode.*;

public class UnauthorizedCreateSlotException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public UnauthorizedCreateSlotException() {
        super(UNAUTHORIZED_CREATE_SLOT.getMessage());
        this.exceptionCode = UNAUTHORIZED_CREATE_SLOT;
    }
}
