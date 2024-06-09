package com.light.backend.member.service;

import com.light.backend.IntegrationTestSupporter;
import com.light.backend.member.controller.dto.request.SignupRequest;
import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.member.exception.ExistsIdException;
import com.light.backend.member.exception.NotFoundMemberException;
import com.light.backend.member.exception.UnauthorizedCreateMemberException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class MemberServiceTest extends IntegrationTestSupporter {

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

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

}