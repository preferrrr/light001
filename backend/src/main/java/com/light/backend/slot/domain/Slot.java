package com.light.backend.slot.domain;

import com.light.backend.global.BaseEntity;
import com.light.backend.member.domain.Member;
import com.light.backend.slot.controller.dto.request.SetSlotDataRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Table(name = "slot", indexes = {
        @Index(name = "idx_origin_mid", columnList = "origin_mid"),
        @Index(name = "idx_mid", columnList = "mid"),
        @Index(name = "idx_end_at", columnList = "end_at"),
        @Index(name = "idx_start_at_end_at_error_state", columnList = "start_at, end_at, slot_error_state")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert @DynamicUpdate
public class Slot extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", nullable = false)
    private Member owner;

    @Column
    private String mid;

    @Column(name = "origin_mid")
    private String originMid;

    @Column(name = "start_at")
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;

    @Column(name = "work_keyword", length = 30)
    private String workKeyword;

    @Column(name = "rank_keyword", length = 30)
    private String rankKeyword;

    @Column(name = "current_rank")
    private int currentRank = 0;

    @Column
    private String description;

    @Column(name = "error_state")
    @Enumerated(EnumType.STRING)
    private SlotErrorState slotErrorState =  SlotErrorState.D;

    @Column(name = "payment_state")
    @Enumerated(EnumType.STRING)
    private SlotPaymentState slotPaymentState = SlotPaymentState.N;

    @Builder
    private Slot(int day, Member owner, LocalDate now) {
        this.owner = owner;
        this.startAt = now.plusDays(1);
        this.endAt = startAt.plusDays(day);
    }

    public static Slot create(int day, Member owner, LocalDate now) {
        return Slot.builder()
                .owner(owner)
                .day(day)
                .now(now)
                .build();
    }

    public void setData(SetSlotDataRequest request) {
        this.mid = request.getMid();
        this.originMid = request.getOriginMid();
        this.workKeyword = request.getWorkKeyword();
        this.rankKeyword = request.getRankKeyword();
        this.description = request.getDescription();
        this.slotPaymentState = request.getSlotPaymentState();
    }

    public void updateErrorState(boolean isError) {
        if (!isError)
            this.slotErrorState = SlotErrorState.Y;
        else
            this.slotErrorState = SlotErrorState.N;
    }

}
