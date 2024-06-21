package com.light.backend.slot.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.member.exception.InvalidMemberRoleException;

public enum SlotPaymentState {

    Y("y", "결제 완료"),
    N("n", "미결제"),
    ;

    private String value;
    private String description;

    SlotPaymentState(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonCreator
    public static SlotPaymentState from(String value) {
        for (SlotPaymentState state : SlotPaymentState.values()) {
            if (state.value.equals(value)) {
                return state;
            }
        }

        return SlotPaymentState.N;
    }

}
