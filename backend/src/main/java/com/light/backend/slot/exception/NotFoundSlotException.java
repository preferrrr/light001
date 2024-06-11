package com.light.backend.slot.exception;

import com.light.backend.global.exception.ExceptionCode;
import com.light.backend.global.exception.code.SlotExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.SlotExceptionCode.*;

public class NotFoundSlotException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public NotFoundSlotException() {
        super(NOT_FOUND_SLOT.getMessage());
        this.exceptionCode = NOT_FOUND_SLOT;
    }
}
