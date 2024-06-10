package com.light.backend.slot.controller.dto.request;

import com.light.backend.member.domain.Member;
import com.light.backend.slot.domain.Slot;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateSlotRequest {
    private String memberId;

    @Builder
    public CreateSlotRequest(String memberId) {
        this.memberId = memberId;
    }

    public Slot toEntity(Member member) {
        return Slot.create(member);
    }
}
