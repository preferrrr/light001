package com.light.backend.slot.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SetSlotDataRequest {

    private Long id;

    private String mid;

    private String originMid;

    private LocalDate startAt;

    private int day;

    private String workKeyword;

    private String rankKeyword;

    private String description;

    @Builder
    public SetSlotDataRequest(Long id, String mid, String originMid, LocalDate startAt, int day, String workKeyword, String rankKeyword, String description) {
        this.id = id;
        this.mid = mid;
        this.originMid = originMid;
        this.startAt = startAt;
        this.day = day;
        this.workKeyword = workKeyword;
        this.rankKeyword = rankKeyword;
        this.description = description;
    }
}
