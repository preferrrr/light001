package com.light.backend.slot.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.SlotExceptionCode.*;

public class InvalidQueryStringForGetSlotsException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public InvalidQueryStringForGetSlotsException() {
        super(INVALID_QUERY_STRING_FOR_GET_SLOTS.getMessage());
        this.exceptionCode = INVALID_QUERY_STRING_FOR_GET_SLOTS;
    }
}
