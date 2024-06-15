package com.light.backend.slot.controller.dto.response;

import com.light.backend.slot.domain.Slot;
import com.light.backend.slot.domain.SlotErrorState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetSlotsResponse {

    private Long id;

    private String ownerId;

    private String adminId;

    private String mid;

    private String originMid;

    private LocalDate startAt;

    private LocalDate endAt;

    private String workKeyword;

    private String rankKeyword;

    private int currentRank;

    private String description;

    private SlotErrorState slotErrorState;

    @Builder
    private GetSlotsResponse(Slot slot) {
        this.id = slot.getId();
        this.ownerId = slot.getOwner().getId();
        this.adminId = slot.getOwner().getCreatedBy().getId();
        this.mid = slot.getMid();
        this.originMid = slot.getOriginMid();
        this.startAt = slot.getStartAt();
        this.endAt = slot.getEndAt();
        this.workKeyword = slot.getWorkKeyword();
        this.rankKeyword = slot.getRankKeyword();
        this.currentRank = slot.getCurrentRank();
        this.description = slot.getDescription();
        this.slotErrorState = slot.getSlotErrorState();
    }

    public static GetSlotsResponse of(Slot slot) {
        return new GetSlotsResponse(slot);
    }
}
