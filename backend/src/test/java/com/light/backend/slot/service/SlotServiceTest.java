package com.light.backend.slot.service;

import com.light.backend.IntegrationTestSupporter;
import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.slot.controller.dto.request.CreateSlotRequest;
import com.light.backend.slot.controller.dto.request.SetSlotDataRequest;
import com.light.backend.slot.domain.SlotErrorState;
import com.light.backend.slot.domain.Slot;
import com.light.backend.slot.exception.NotCreatedByAdminException;
import com.light.backend.slot.exception.UnauthorizedCreateSlotException;
import com.light.backend.slot.exception.UnauthorizedSetSlotDataException;
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
        assertThat(result.getCurrentRank()).isEqualTo(0);
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

    @DisplayName("슬롯 사용자가 슬롯의 데이터를 수정한다.")
    @Test
    void setSlotData_success1() {

        /** given */

        doReturn(MEMBER).when(currentMemberGetter).getCurrentMemberId();

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        Member member = Member.create(MEMBER, PASSWORD, MemberRole.MEMBER, admin);
        memberRepository.saveAll(List.of(admin, member));

        Slot slot = Slot.create(member);
        slotRepository.save(slot);

        SetSlotDataRequest request = SetSlotDataRequest.builder()
                .id(slot.getId())
                .mid(MID)
                .originMid(ORIGIN_MID)
                .rankKeyword(RANK_KEYWORD)
                .workKeyword(WORK_KEYWORD)
                .day(10)
                .description(DESCRIPTION)
                .build();

        /** when */

        slotService.setSlotData(request);

        /** then */

        Slot result = slotRepository.findById(slot.getId()).get();
        assertThat(result.getOwner().getId()).isEqualTo(MEMBER);
        assertThat(result.getMid()).isEqualTo(MID);
        assertThat(result.getOriginMid()).isEqualTo(ORIGIN_MID);
        assertThat(result.getRankKeyword()).isEqualTo(RANK_KEYWORD);
        assertThat(result.getWorkKeyword()).isEqualTo(WORK_KEYWORD);
        assertThat(result.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(result.getSlotErrorState()).isEqualTo(SlotErrorState.N);
        assertThat(result.getCurrentRank()).isEqualTo(0);
        assertThat(result.getEndAt().minusDays(10)).isEqualTo(result.getStartAt());

    }

    @DisplayName("담당 관리자가 슬롯의 데이터를 수정한다.")
    @Test
    void setSlotData_success2() {

        /** given */

        doReturn(ADMIN).when(currentMemberGetter).getCurrentMemberId();

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        Member member = Member.create(MEMBER, PASSWORD, MemberRole.MEMBER, admin);
        memberRepository.saveAll(List.of(admin, member));

        Slot slot = Slot.create(member);
        slotRepository.save(slot);

        SetSlotDataRequest request = SetSlotDataRequest.builder()
                .id(slot.getId())
                .mid(MID)
                .originMid(ORIGIN_MID)
                .rankKeyword(RANK_KEYWORD)
                .workKeyword(WORK_KEYWORD)
                .day(10)
                .description(DESCRIPTION)
                .build();

        /** when */

        slotService.setSlotData(request);

        /** then */

        Slot result = slotRepository.findById(slot.getId()).get();
        assertThat(result.getOwner().getId()).isEqualTo(MEMBER);
        assertThat(result.getMid()).isEqualTo(MID);
        assertThat(result.getOriginMid()).isEqualTo(ORIGIN_MID);
        assertThat(result.getRankKeyword()).isEqualTo(RANK_KEYWORD);
        assertThat(result.getWorkKeyword()).isEqualTo(WORK_KEYWORD);
        assertThat(result.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(result.getSlotErrorState()).isEqualTo(SlotErrorState.N);
        assertThat(result.getCurrentRank()).isEqualTo(0);
        assertThat(result.getEndAt().minusDays(10)).isEqualTo(result.getStartAt());

    }


    @DisplayName("MASTER가 슬롯의 데이터를 수정한다.")
    @Test
    void setSlotData_success3() {

        /** given */

        doReturn(MASTER).when(currentMemberGetter).getCurrentMemberId();

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        Member member = Member.create(MEMBER, PASSWORD, MemberRole.MEMBER, admin);
        memberRepository.saveAll(List.of(admin, member));

        Slot slot = Slot.create(member);
        slotRepository.save(slot);

        SetSlotDataRequest request = SetSlotDataRequest.builder()
                .id(slot.getId())
                .mid(MID)
                .originMid(ORIGIN_MID)
                .rankKeyword(RANK_KEYWORD)
                .workKeyword(WORK_KEYWORD)
                .day(10)
                .description(DESCRIPTION)
                .build();

        /** when */

        slotService.setSlotData(request);

        /** then */

        Slot result = slotRepository.findById(slot.getId()).get();
        assertThat(result.getOwner().getId()).isEqualTo(MEMBER);
        assertThat(result.getMid()).isEqualTo(MID);
        assertThat(result.getOriginMid()).isEqualTo(ORIGIN_MID);
        assertThat(result.getRankKeyword()).isEqualTo(RANK_KEYWORD);
        assertThat(result.getWorkKeyword()).isEqualTo(WORK_KEYWORD);
        assertThat(result.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(result.getSlotErrorState()).isEqualTo(SlotErrorState.N);
        assertThat(result.getCurrentRank()).isEqualTo(0);
        assertThat(result.getEndAt().minusDays(10)).isEqualTo(result.getStartAt());

    }

    @DisplayName("담당 관리자가 아니면, UnauthorizedSetSlotDataException을 반환한다.")
    @Test
    void setSlotData_fail1() {

        /** given */

        doReturn(ADMIN_2).when(currentMemberGetter).getCurrentMemberId();

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        Member admin2 = Member.create(ADMIN_2, PASSWORD, MemberRole.ADMIN, master);
        Member member = Member.create(MEMBER, PASSWORD, MemberRole.MEMBER, admin);
        memberRepository.saveAll(List.of(admin, admin2, member));

        Slot slot = Slot.create(member);
        slotRepository.save(slot);

        final long mid = 1111111111l;
        final long originMid = 2222222222l;

        SetSlotDataRequest request = SetSlotDataRequest.builder()
                .id(slot.getId())
                .mid(MID)
                .originMid(ORIGIN_MID)
                .rankKeyword("rank keyword")
                .workKeyword("work keyword")
                .day(10)
                .description("description")
                .build();

        /** when then */

        assertThatThrownBy(() -> slotService.setSlotData(request))
                .isInstanceOf(UnauthorizedSetSlotDataException.class);

    }

    @DisplayName("슬롯 사용자가 아니면, UnauthorizedSetSlotDataException을 반환한다.")
    @Test
    void setSlotData_fail2() {

        /** given */

        doReturn(MEMBER_2).when(currentMemberGetter).getCurrentMemberId();

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        Member member2 = Member.create(MEMBER_2, PASSWORD, MemberRole.MEMBER, admin);
        Member member = Member.create(MEMBER, PASSWORD, MemberRole.MEMBER, admin);
        memberRepository.saveAll(List.of(admin, member, member2));

        Slot slot = Slot.create(member);
        slotRepository.save(slot);

        final long mid = 1111111111l;
        final long originMid = 2222222222l;

        SetSlotDataRequest request = SetSlotDataRequest.builder()
                .id(slot.getId())
                .mid(MID)
                .originMid(ORIGIN_MID)
                .rankKeyword("rank keyword")
                .workKeyword("work keyword")
                .day(10)
                .description("description")
                .build();

        /** when then */

        assertThatThrownBy(() -> slotService.setSlotData(request))
                .isInstanceOf(UnauthorizedSetSlotDataException.class);

    }
}