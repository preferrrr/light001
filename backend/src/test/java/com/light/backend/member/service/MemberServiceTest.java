package com.light.backend.member.service;

import com.light.backend.IntegrationTestSupporter;
import com.light.backend.member.controller.dto.request.LoginRequest;
import com.light.backend.member.controller.dto.request.SignupRequest;
import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.member.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

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

        assertThatThrownBy(()-> memberService.reissueJwt("cookie"))
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

        assertThatThrownBy(()-> memberService.reissueJwt("cookie"))
                .isInstanceOf(NotFoundMemberByRefreshTokenException.class);

    }

}