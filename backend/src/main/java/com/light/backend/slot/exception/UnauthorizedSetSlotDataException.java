package com.light.backend.slot.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.SlotExceptionCode.*;

public class UnauthorizedSetSlotDataException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public UnauthorizedSetSlotDataException() {
        super(UNAUTHORIZED_SET_SLOT_DATA.getMessage());
        this.exceptionCode = UNAUTHORIZED_SET_SLOT_DATA;
    }
}
