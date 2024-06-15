package com.light.backend.member.service;

import com.light.backend.IntegrationTestSupporter;
import com.light.backend.member.controller.dto.request.LoginRequest;
import com.light.backend.member.controller.dto.request.SignupRequest;
import com.light.backend.member.controller.dto.response.GetMembersResponse;
import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.member.exception.*;
import com.light.backend.slot.controller.dto.request.SetSlotDataRequest;
import com.light.backend.slot.controller.dto.response.GetDashboardResponse;
import com.light.backend.slot.domain.Slot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class MemberServiceTest extends IntegrationTestSupporter {

    @DisplayName("MASTER가 ADMIN을 회원가입 시켜준다.")
    @Test
    void signUpTest_success1() {

        /** given */

        given(currentMemberGetter.getCurrentMemberId()).willReturn(MASTER);

        SignupRequest request = SignupRequest.builder()
                .id(ADMIN)
                .password(PASSWORD)
                .role(MemberRole.ADMIN)
                .build();


        /** when */

        memberService.signup(request);

        /** then */

        Member result = memberRepository.findById(ADMIN).get();
        assertThat(result.getId()).isEqualTo(ADMIN);
        assertThat(result.getRole()).isEqualTo(MemberRole.ADMIN);
        assertThat(result.getCreatedBy().getId()).isEqualTo(MASTER);

    }

    @DisplayName("ADMIN이 MEMBER를 회원가입 시켜준다.")
    @Test
    void signUpTest_success2() {

        /** given */

        given(currentMemberGetter.getCurrentMemberId()).willReturn(ADMIN);

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        memberRepository.save(admin);

        SignupRequest request = SignupRequest.builder()
                .id(MEMBER)
                .password(PASSWORD)
                .role(MemberRole.MEMBER)
                .build();

        /** when */

        memberService.signup(request);

        /** then */

        Member result = memberRepository.findById(MEMBER).get();
        assertThat(result.getId()).isEqualTo(MEMBER);
        assertThat(result.getRole()).isEqualTo(MemberRole.MEMBER);
        assertThat(result.getCreatedBy().getId()).isEqualTo(ADMIN);

    }

    @DisplayName("이미 존재하는 아이디이면, ExistsIdException을 반환한다.")
    @Test
    void signUpTest_fail1() {

        /** given */

        given(currentMemberGetter.getCurrentMemberId()).willReturn(MASTER);

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        memberRepository.save(admin);

        SignupRequest request = SignupRequest.builder()
                .id(ADMIN)
                .password(PASSWORD)
                .role(MemberRole.ADMIN)
                .build();

        /** when then */

        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(ExistsIdException.class);

    }

    @DisplayName("ADMIN이 생성하는 사용자 권한이 MEMBER가 아니면, UnauthorizedCreateMemberException을 반환한다.")
    @Test
    void signUpTest_fail2() {

        /** given */

        given(currentMemberGetter.getCurrentMemberId()).willReturn(ADMIN);

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        memberRepository.save(admin);

        SignupRequest request = SignupRequest.builder()
                .id("admin2")
                .password(PASSWORD)
                .role(MemberRole.ADMIN)
                .build();

        /** when then */

        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(UnauthorizedCreateMemberException.class);

    }

    @DisplayName("현재 사용자 id가 존재하지 않으면, NotFoundMemberException을 반환한다.")
    @Test
    void signUpTest_fail3() {

        /** given */

        given(currentMemberGetter.getCurrentMemberId()).willReturn("testId");

        SignupRequest request = SignupRequest.builder()
                .id(ADMIN)
                .password(PASSWORD)
                .role(MemberRole.ADMIN)
                .build();

        /** when then */

        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(NotFoundMemberException.class);

    }

    @DisplayName("로그인에 성공하면, access token과 refresh token이 포함된 헤더를 반환한다.")
    @Test
    void loginTest_success() {

        /** given */

        doReturn(ACCESS_TOKEN).when(jwtProvider).createAccessToken(anyString(), any(MemberRole.class));
        doReturn(REFRESH_TOKEN_VALUE).when(memberServiceSupport).createRefreshTokenValue();
        doReturn(REFRESH_TOKEN).when(jwtProvider).createRefreshToken(anyString());

        LoginRequest loginRequest = LoginRequest.builder()
                .id(MASTER)
                .password(PASSWORD)
                .build();

        /** when */

        HttpHeaders resultHeader = memberService.login(loginRequest);

        /** then */

        Member member = memberRepository.findById(MASTER).get();

        assertThat(member.getRefreshTokenValue()).isEqualTo(REFRESH_TOKEN_VALUE);
        assertThat(resultHeader.get("Authorization").get(0)).isEqualTo(ACCESS_TOKEN);
        assertThat(resultHeader.get("Cookie").get(0)).isNotBlank();

    }

    @DisplayName("비밀번호가 틀리면, NotMatchPasswordException을 반환한다.")
    @Test
    void loginTest_fail() {

        /** given */
        LoginRequest loginRequest = LoginRequest.builder()
                .id(MASTER)
                .password("failPassword")
                .build();

        /** when then */

        assertThatThrownBy(() -> memberService.login(loginRequest))
                .isInstanceOf(NotMatchPasswordException.class);

    }

    @DisplayName("refresh token 검증에 성공하면, access token과 refresh token이 포함된 헤더를 반환한다.")
    @Test
    void reissueJwt_success() {

        /** given */

        final String newRefreshTokenValue = "newRefreshTokenValue";

        doReturn(REFRESH_TOKEN).when(memberServiceSupport).getRefreshToken(anyString());
        doNothing().when(memberServiceSupport).verifyRefreshToken(anyString());
        doReturn(REFRESH_TOKEN_VALUE).when(memberServiceSupport).getRefreshTokenValue(anyString());
        doReturn(newRefreshTokenValue).when(memberServiceSupport).createRefreshTokenValue();
        doReturn(ACCESS_TOKEN).when(jwtProvider).createAccessToken(anyString(), any(MemberRole.class));
        doReturn(REFRESH_TOKEN).when(jwtProvider).createRefreshToken(anyString());

        /** when */

        HttpHeaders resultHeader = memberService.reissueJwt("cookie");

        /** then */

        Member member = memberRepository.findById(MASTER).get();
        assertThat(member.getRefreshTokenValue()).isEqualTo(newRefreshTokenValue);
        assertThat(resultHeader.get("Authorization").get(0)).isEqualTo(ACCESS_TOKEN);
        assertThat(resultHeader.get("Cookie").get(0)).isNotBlank();

    }

    @DisplayName("유효하지 않은 refresh token이라면, InvalidRefreshTokenException을 반환한다.")
    @Test
    void reissueJwt_fail1() {

        /** given */

        doReturn("invalidRefreshToken").when(memberServiceSupport).getRefreshToken(anyString());

        /** when then */

        assertThatThrownBy(() -> memberService.reissueJwt("cookie"))
                .isInstanceOf(InvalidRefreshTokenException.class);

    }

    @DisplayName("refresh token value로 사용자를 찾을 수 없으면, NotFoundMemberByRefreshTokenException을 반환한다.")
    @Test
    void reissueJwt_fail2() {

        /** given */

        final String invalidRefreshToken = "invalidRefreshToken";
        final String invalidRefreshTokenValue = "invalidRefreshTokenValue";

        doReturn(invalidRefreshToken).when(memberServiceSupport).getRefreshToken(anyString());
        doNothing().when(memberServiceSupport).verifyRefreshToken(anyString());
        doReturn(invalidRefreshTokenValue).when(memberServiceSupport).getRefreshTokenValue(anyString());

        /** when then */

        assertThatThrownBy(() -> memberService.reissueJwt("cookie"))
                .isInstanceOf(NotFoundMemberByRefreshTokenException.class);

    }

    @DisplayName("MEMBER가 대시보드를 조회하면, 자신거만 슬롯 통계를 구한다.")
    @Test
    void getDashboard_success3() {

        /** given */

        doReturn(MEMBER).when(currentMemberGetter).getCurrentMemberId();

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        Member member = Member.create(MEMBER, PASSWORD, MemberRole.MEMBER, admin);
        Member member2 = Member.create(MEMBER_2, PASSWORD, MemberRole.MEMBER, master);
        memberRepository.saveAll(List.of(admin, member, member2));

        Slot slot1 = Slot.create(admin);
        slot1.setData(
                SetSlotDataRequest.builder()
                        .mid(MID)
                        .originMid(ORIGIN_MID)
                        .startAt(LocalDate.of(2024, 1, 5)) // 만료 슬롯
                        .rankKeyword(RANK_KEYWORD)
                        .workKeyword(WORK_KEYWORD)
                        .description(DESCRIPTION)
                        .day(9)
                        .build()
        );

        Slot slot2 = Slot.create(member);
        slot2.setData(
                SetSlotDataRequest.builder()
                        .mid(MID)
                        .originMid(ORIGIN_MID)
                        .startAt(LocalDate.of(2024, 1, 4)) // 만료된지 이틀 지났기 때문에 아무 슬롯 아님
                        .rankKeyword(RANK_KEYWORD)
                        .workKeyword(WORK_KEYWORD)
                        .description(DESCRIPTION)
                        .day(9)
                        .build()
        );

        Slot slot3 = Slot.create(member);
        slot3.setData(
                SetSlotDataRequest.builder()
                        .mid(MID)
                        .originMid(ORIGIN_MID)
                        .startAt(LocalDate.of(2024, 1, 6)) // 만료 예정 슬롯, 구동 슬롯
                        .rankKeyword(RANK_KEYWORD)
                        .workKeyword(WORK_KEYWORD)
                        .description(DESCRIPTION)
                        .day(9)
                        .build()
        );
        slot3.updateErrorState(true); // 오류 슬롯

        Slot slot4 = Slot.create(member);
        slot4.setData(
                SetSlotDataRequest.builder()
                        .mid(MID)
                        .originMid(ORIGIN_MID)
                        .startAt(LocalDate.of(2024, 1, 10)) // 구동 슬롯
                        .rankKeyword(RANK_KEYWORD)
                        .workKeyword(WORK_KEYWORD)
                        .description(DESCRIPTION)
                        .day(9)
                        .build()
        );

        Slot slot5 = Slot.create(member2); //대기 슬롯

        slotRepository.saveAll(List.of(slot1, slot2, slot3, slot4, slot5));

        /** when */

        GetDashboardResponse response = slotService.getDashboard(LocalDate.of(2024, 1, 15));
        //endAt이14일 인것만 만료 슬롯으로 조회되어야 함.

        /** then */

        assertThat(response.getTotal()).isEqualTo(3);
        assertThat(response.getClosed()).isEqualTo(0);
        assertThat(response.getRunning()).isEqualTo(2);
        assertThat(response.getWaiting()).isEqualTo(0);
        assertThat(response.getExpiring()).isEqualTo(1);
        assertThat(response.getError()).isEqualTo(1);

    }

    @DisplayName("MASTER가 회원 리스트를 조회한다.")
    @Test
    void getMembers_success1() {

        /** given */
        doReturn(MASTER).when(currentMemberGetter).getCurrentMemberId();

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        Member member1 = Member.create(MEMBER, PASSWORD, MemberRole.MEMBER, admin);
        Member member2 = Member.create(MEMBER_2, PASSWORD, MemberRole.MEMBER, admin);
        memberRepository.saveAll(List.of(admin, member1, member2));

        Slot slot1 = Slot.create(admin);
        slot1.setData(
                SetSlotDataRequest.builder()
                        .mid(MID)
                        .originMid(ORIGIN_MID)
                        .startAt(LocalDate.of(2024, 1, 5)) // 만료 슬롯, 끝난지 하루 된거.
                        .rankKeyword(RANK_KEYWORD)
                        .workKeyword(WORK_KEYWORD)
                        .description(DESCRIPTION)
                        .day(9)
                        .build()
        );

        Slot slot2 = Slot.create(member1);
        slot2.setData(
                SetSlotDataRequest.builder()
                        .mid(MID)
                        .originMid(ORIGIN_MID)
                        .startAt(LocalDate.of(2024, 1, 4)) // 만료된지 이틀 지났기 때문에 아무 슬롯 아님
                        .rankKeyword(RANK_KEYWORD)
                        .workKeyword(WORK_KEYWORD)
                        .description(DESCRIPTION)
                        .day(9)
                        .build()
        );

        Slot slot3 = Slot.create(member1);
        slot3.setData(
                SetSlotDataRequest.builder()
                        .mid(MID)
                        .originMid(ORIGIN_MID)
                        .startAt(LocalDate.of(2024, 1, 6)) // 구동 슬롯
                        .rankKeyword(RANK_KEYWORD)
                        .workKeyword(WORK_KEYWORD)
                        .description(DESCRIPTION)
                        .day(9)
                        .build()
        );
        slot3.updateErrorState(true); // 오류 슬롯

        Slot slot4 = Slot.create(member1);
        slot4.setData(
                SetSlotDataRequest.builder()
                        .mid(MID)
                        .originMid(ORIGIN_MID)
                        .startAt(LocalDate.of(2024, 1, 10)) // 구동 슬롯
                        .rankKeyword(RANK_KEYWORD)
                        .workKeyword(WORK_KEYWORD)
                        .description(DESCRIPTION)
                        .day(9)
                        .build()
        );

        Slot slot5 = Slot.create(member2); //대기 슬롯

        slotRepository.saveAll(List.of(slot1, slot2, slot3, slot4, slot5));

        /** when */

        Page<GetMembersResponse> result = memberService.getMembers(PageRequest.of(0, 10), LocalDate.of(2024, 1, 15));

        /** then */
        assertThat(result.getTotalElements()).isEqualTo(4);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getNumber()).isEqualTo(0); //현재 페이지 번호
        assertThat(result.getSize()).isEqualTo(10); // 페이지당 요소 개수

        assertThat(result.getContent()).hasSize(4);

        for (GetMembersResponse response : result.getContent()) {
            if (response.getMember().equals(ADMIN)) {

                assertThat(response.getTotal()).isEqualTo(5);
                assertThat(response.getRunning()).isEqualTo(2);
                assertThat(response.getClosed()).isEqualTo(1);
                assertThat(response.getAdmin()).isEqualTo(MASTER);

            } else if (response.getMember().equals(MEMBER)) {

                assertThat(response.getTotal()).isEqualTo(3);
                assertThat(response.getRunning()).isEqualTo(2);
                assertThat(response.getClosed()).isEqualTo(0);
                assertThat(response.getAdmin()).isEqualTo(ADMIN);

            } else if (response.getMember().equals(MEMBER_2)) {
                assertThat(response.getTotal()).isEqualTo(1);
                assertThat(response.getRunning()).isEqualTo(0);
                assertThat(response.getClosed()).isEqualTo(0);
                assertThat(response.getAdmin()).isEqualTo(ADMIN);
            }
        }


    }

    @DisplayName("ADMIN이 회원 리스트를 조회한다.")
    @Test
    void getMembers_success2() {

        /** given */
        doReturn(ADMIN).when(currentMemberGetter).getCurrentMemberId();

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        Member admin2 = Member.create(ADMIN_2, PASSWORD, MemberRole.ADMIN, master);
        Member member1 = Member.create(MEMBER, PASSWORD, MemberRole.MEMBER, admin);
        Member member2 = Member.create(MEMBER_2, PASSWORD, MemberRole.MEMBER, admin2);
        memberRepository.saveAll(List.of(admin, admin2, member1, member2));

        Slot slot1 = Slot.create(admin);
        slot1.setData(
                SetSlotDataRequest.builder()
                        .mid(MID)
                        .originMid(ORIGIN_MID)
                        .startAt(LocalDate.of(2024, 1, 5)) // 만료 슬롯, 끝난지 하루 된거.
                        .rankKeyword(RANK_KEYWORD)
                        .workKeyword(WORK_KEYWORD)
                        .description(DESCRIPTION)
                        .day(9)
                        .build()
        );

        Slot slot2 = Slot.create(member1);
        slot2.setData(
                SetSlotDataRequest.builder()
                        .mid(MID)
                        .originMid(ORIGIN_MID)
                        .startAt(LocalDate.of(2024, 1, 4)) // 만료된지 이틀 지났기 때문에 아무 슬롯 아님
                        .rankKeyword(RANK_KEYWORD)
                        .workKeyword(WORK_KEYWORD)
                        .description(DESCRIPTION)
                        .day(9)
                        .build()
        );

        Slot slot3 = Slot.create(member1);
        slot3.setData(
                SetSlotDataRequest.builder()
                        .mid(MID)
                        .originMid(ORIGIN_MID)
                        .startAt(LocalDate.of(2024, 1, 6)) // 구동 슬롯
                        .rankKeyword(RANK_KEYWORD)
                        .workKeyword(WORK_KEYWORD)
                        .description(DESCRIPTION)
                        .day(9)
                        .build()
        );
        slot3.updateErrorState(true); // 오류 슬롯

        Slot slot4 = Slot.create(member1);
        slot4.setData(
                SetSlotDataRequest.builder()
                        .mid(MID)
                        .originMid(ORIGIN_MID)
                        .startAt(LocalDate.of(2024, 1, 10)) // 구동 슬롯
                        .rankKeyword(RANK_KEYWORD)
                        .workKeyword(WORK_KEYWORD)
                        .description(DESCRIPTION)
                        .day(9)
                        .build()
        );

        Slot slot5 = Slot.create(member2); //대기 슬롯

        slotRepository.saveAll(List.of(slot1, slot2, slot3, slot4, slot5));

        /** when */

        Page<GetMembersResponse> result = memberService.getMembers(PageRequest.of(0, 10), LocalDate.of(2024, 1, 15));

        /** then */

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getNumber()).isEqualTo(0); //현재 페이지 번호
        assertThat(result.getSize()).isEqualTo(10); // 페이지당 요소 개수

        assertThat(result.getContent()).hasSize(1);

        assertThat(result.getContent().get(0).getMember()).isEqualTo(MEMBER);
        assertThat(result.getContent().get(0).getTotal()).isEqualTo(3);
        assertThat(result.getContent().get(0).getRunning()).isEqualTo(2);
        assertThat(result.getContent().get(0).getClosed()).isEqualTo(0);
        assertThat(result.getContent().get(0).getAdmin()).isEqualTo(ADMIN);

    }

    @DisplayName("회원 리스트를 조회할 때, MASTER나 ADMIN이 아니면, UnauthorizedGetMembersException을 반환한다.")
    @Test
    void getMembers_fail1() {

        /** given */
        doReturn(MEMBER).when(currentMemberGetter).getCurrentMemberId();

        Member admin = Member.create(ADMIN, PASSWORD, MemberRole.ADMIN, master);
        Member member = Member.create(MEMBER, PASSWORD, MemberRole.MEMBER, admin);
        memberRepository.saveAll(List.of(admin, member));

        /** when then */
        assertThatThrownBy(() -> memberService.getMembers(PageRequest.of(0, 10), LocalDate.of(2024, 1, 15)))
                .isInstanceOf(UnauthorizedGetMembersException.class);

    }
}