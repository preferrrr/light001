package com.light.backend.slot.domain;

import com.light.backend.global.BaseEntity;
import com.light.backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Table(name = "slot")
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
    private Long mid;

    @Column(name = "origin_mid")
    private Long originMid;

    @Column(name = "start_at")
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;

    @Column(name = "work_keyword", length = 30)
    private String workKeyword;

    @Column(name = "rank_keyword", length = 30)
    private String rankKeyword;

    @Column
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private SlotErrorState slotErrorState =  SlotErrorState.D;

    @Builder
    private Slot(Member owner) {
        this.owner = owner;
    }

    public static Slot create(Member owner) {
        return Slot.builder()
                .owner(owner)
                .build();
    }

}
