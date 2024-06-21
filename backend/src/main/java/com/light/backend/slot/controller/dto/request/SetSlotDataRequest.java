package com.light.backend.slot.controller.dto.request;

import com.light.backend.slot.domain.SlotPaymentState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SetSlotDataRequest {

    @NotNull(message = "id는 null 또는 공백일 수 없습니다.")
    private Long id;

    @NotBlank(message = "mid는 null 또는 공백일 수 없습니다.")
    private String mid;

    private String originMid;

    @NotBlank(message = "작업 키워드는 null 또는 공백일 수 없습니다.")
    private String workKeyword;

    @NotBlank(message = "순위 키워드는 null 또는 공백일 수 없습니다.")
    private String rankKeyword;

    private String description;

    private SlotPaymentState slotPaymentState;

    @Builder
    public SetSlotDataRequest(Long id, String mid, String originMid, LocalDate startAt, int day, String workKeyword, String rankKeyword, String description, SlotPaymentState slotPaymentState) {
        this.id = id;
        this.mid = mid;
        this.originMid = originMid;
        this.workKeyword = workKeyword;
        this.rankKeyword = rankKeyword;
        this.description = description;
        this.slotPaymentState = slotPaymentState;
    }
}
