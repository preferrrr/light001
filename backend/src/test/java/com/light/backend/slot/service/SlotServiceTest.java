package com.light.backend.slot.service;

import com.light.backend.IntegrationTestSupporter;
import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.slot.controller.dto.request.CreateSlotRequest;
import com.light.backend.slot.domain.SlotErrorState;
import com.light.backend.slot.domain.Slot;
import com.light.backend.slot.exception.NotCreatedByAdminException;
import com.light.backend.slot.exception.UnauthorizedCreateSlotException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

class SlotServiceTest extends IntegrationTestSupporter {

    @DisplayName("슬롯을 생성하고, 슬롯은 id, member, error를 제외하고 모두 null이다.")
    @Test
    void createSlot_success() {

        /** given */

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        Member member = Member.create(MEMBER, PASSWORD, MemberRole.MEMBER, admin);
        memberRepository.saveAll(List.of(admin, member));

        doReturn(ADMIN).when(currentMemberGetter).getCurrentMemberId();

        CreateSlotRequest request = CreateSlotRequest.builder()
                .memberId(MEMBER)
                .build();

        /** when */

        slotService.createSlot(request);

        /** then */

        List<Slot> slots = slotRepository.findAll();
        assertThat(slots).hasSize(1);

        Slot result = slots.get(0);
        assertThat(result.getOwner().getId()).isEqualTo(MEMBER);
        assertThat(result.getMid()).isNull();
        assertThat(result.getOriginMid()).isNull();
        assertThat(result.getStartAt()).isNull();
        assertThat(result.getEndAt()).isNull();
        assertThat(result.getDescription()).isNull();
        assertThat(result.getRankKeyword()).isNull();
        assertThat(result.getWorkKeyword()).isNull();
        assertThat(result.getSlotErrorState()).isEqualTo(SlotErrorState.D);
    }

    @DisplayName("관리자가 아니면, UnauthorizedCreateSlotException을 반환한다.")
    @Test
    void createSlot_fail1() {

        /** given */

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        Member member = Member.create(MEMBER, PASSWORD, MemberRole.MEMBER, admin);
        memberRepository.saveAll(List.of(admin, member));

        doReturn(MEMBER).when(currentMemberGetter).getCurrentMemberId();

        CreateSlotRequest request = CreateSlotRequest.builder()
                .memberId(MEMBER)
                .build();

        /** when then */

        assertThatThrownBy(() -> slotService.createSlot(request))
                .isInstanceOf(UnauthorizedCreateSlotException.class);

    }

    @DisplayName("관리자가 생성해준 멤버가 아니면 NotCreatedByAdminException을 반환한다..")
    @Test
    void createSlot_fail2() {

        /** given */

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        Member admin2 = Member.create(ADMIN_2, PASSWORD, MemberRole.ADMIN, master);
        Member member = Member.create(MEMBER, PASSWORD, MemberRole.MEMBER, admin);
        memberRepository.saveAll(List.of(admin, admin2, member));

        doReturn(ADMIN_2).when(currentMemberGetter).getCurrentMemberId();

        CreateSlotRequest request = CreateSlotRequest.builder()
                .memberId(MEMBER)
                .build();

        /** when then */

        assertThatThrownBy(() -> slotService.createSlot(request))
                .isInstanceOf(NotCreatedByAdminException.class);

    }

}