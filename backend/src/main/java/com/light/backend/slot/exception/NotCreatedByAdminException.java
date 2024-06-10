package com.light.backend.slot.exception;

import com.light.backend.global.exception.ExceptionCode;
import lombok.Getter;

import static com.light.backend.global.exception.code.SlotExceptionCode.NOT_CREATED_BY_ADMIN;

public class NotCreatedByAdminException extends RuntimeException {
    @Getter
    private ExceptionCode exceptionCode;

    public NotCreatedByAdminException() {
        super(NOT_CREATED_BY_ADMIN.getMessage());
        this.exceptionCode = NOT_CREATED_BY_ADMIN;
    }
}
