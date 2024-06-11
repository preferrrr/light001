package com.light.backend.slot.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SetSlotDataRequest {

    private Long id;

    private String mid;

    private String originMid;

    private int day;

    private String workKeyword;

    private String rankKeyword;

    private String description;

    @Builder
    public SetSlotDataRequest(Long id, String mid, String originMid, int day, String workKeyword, String rankKeyword, String description) {
        this.id = id;
        this.mid = mid;
        this.originMid = originMid;
        this.day = day;
        this.workKeyword = workKeyword;
        this.rankKeyword = rankKeyword;
        this.description = description;
    }
}
