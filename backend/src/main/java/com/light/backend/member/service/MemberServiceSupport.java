package com.light.backend.member.service;

import com.light.backend.global.jwt.JwtProvider;
import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.member.exception.*;
import com.light.backend.member.repository.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.light.backend.member.domain.MemberRole.*;


@Service
@RequiredArgsConstructor
public class MemberServiceSupport {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    private static final String AUTHORIZATION = "Authorization";
    private static final String REFRESH_TOKEN = "refreshToken";

    public Member getMemberById(String id) {
        return memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
    }

    public void checkAuthority(MemberRole currentMemberRole, MemberRole createMemberRole) {
        if (currentMemberRole.equals(MEMBER) || (currentMemberRole.equals(ADMIN) && !createMemberRole.equals(MEMBER)))
            throw new UnauthorizedCreateMemberException();
    }

    public void checkIdExists(String id) {
        if (memberRepository.existsById(id)) {
            throw new ExistsIdException();
        }
    }

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void save(Member member) {
        memberRepository.save(member);
    }

    public void checkPassword(String requestPassword, String originPassword) {
        if (!passwordEncoder.matches(requestPassword, originPassword))
            throw new NotMatchPasswordException();
    }

    public String createRefreshTokenValue() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public HttpHeaders createJwt(String email, MemberRole role, String refreshTokenValue) {
        HttpHeaders headers = new HttpHeaders();

        headers.add(AUTHORIZATION, jwtProvider.createAccessToken(email, role)); // access token

        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN, jwtProvider.createRefreshToken(refreshTokenValue))
                .maxAge(14 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();

        headers.add(HttpHeaders.COOKIE, cookie.toString()); // refresh token

        return headers;
    }

    public void verifyRefreshToken(String refreshToken) {
        jwtProvider.verifyRefreshToken(refreshToken);
    }

    public Member getMemberByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshTokenValue(refreshToken).orElseThrow(NotFoundMemberByRefreshTokenException::new);
    }

    public String getRefreshToken(String cookie) {
        return cookie.substring(REFRESH_TOKEN.length() + 1, cookie.indexOf(";")) ;
    }

    public String getRefreshTokenValue(String refreshToken) {
        return jwtProvider.getValue(refreshToken);
    }



}
