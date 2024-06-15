package com.light.backend.slot.controller.dto.request;

import com.light.backend.member.domain.Member;
import com.light.backend.slot.domain.Slot;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
