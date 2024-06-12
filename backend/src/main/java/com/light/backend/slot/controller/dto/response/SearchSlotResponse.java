package com.light.backend.slot.controller.dto.response;

import com.light.backend.member.domain.Member;
import com.light.backend.slot.domain.Slot;
import com.light.backend.slot.domain.SlotErrorState;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import javax.naming.directory.SearchResult;
import java.time.LocalDate;

@Getter
public class SearchSlotResponse {

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
    private SearchSlotResponse(Slot slot) {
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

    public static SearchSlotResponse of(Slot slot) {
        return new SearchSlotResponse(slot);
    }
}
