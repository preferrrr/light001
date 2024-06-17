package com.light.backend.slot.controller.dto.request;

import com.light.backend.member.domain.Member;
import com.light.backend.slot.domain.Slot;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateSlotRequest {

    @NotBlank(message = "사용자는 null 또는 공백일 수 없습니다.")
    private String memberId;

    @NotNull(message = "일수는 필수입니다.")
    private int day;

    @Builder
    public CreateSlotRequest(String memberId) {
        this.memberId = memberId;
    }

    public Slot toEntity(Member member, LocalDate now) {
        return Slot.create(day, member, now);
    }
}
