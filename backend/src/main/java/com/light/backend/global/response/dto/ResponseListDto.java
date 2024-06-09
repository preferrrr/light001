package com.light.backend.global.response.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ResponseListDto<T> {

    private List<T> data;
    private String message;

    public ResponseListDto(List<T> data, String message) {
        this.data = data;
        this.message = message;
    }
}
