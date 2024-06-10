package com.light.backend.slot.domain;

public enum SlotErrorState {

    Y("y", "오류 상태"),
    N("n", "정상 상태"),
    D("d", "디폴트");

    private String value;
    private String description;

    SlotErrorState(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
