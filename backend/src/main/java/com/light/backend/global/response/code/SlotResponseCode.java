package com.light.backend.global.response.code;

import com.light.backend.global.response.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Getter
@RequiredArgsConstructor
public enum SlotResponseCode implements SuccessCode {

    CREATE_SLOT(CREATED, "S01","슬롯 생성에 성공했습니다."),
    SET_SLOT_DATA(OK, "S02","슬롯 데이터 저장에 성공했습니다."),
    GET_SLOTS(OK, "S03","슬롯 리스트 조회에 성공했습니다."),
    GET_DASHBOARD(OK, "S04", "대시 보드 조회에 성공했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}