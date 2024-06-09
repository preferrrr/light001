package com.light.backend.global.response.dto;

import lombok.Getter;

@Getter
public class ResponseSingleDto<T> {

    private T data;
    private String message;

    public ResponseSingleDto(T data, String message) {
        this.data = data;
        this.message = message;
    }

}
