package com.light.backend.member.controller;

import com.light.backend.global.response.ApiResponse;
import com.light.backend.member.controller.dto.request.LoginRequest;
import com.light.backend.member.controller.dto.request.SignupRequest;
import com.light.backend.member.controller.dto.response.GetMembersResponse;
import com.light.backend.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.light.backend.global.response.code.MemberResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 가입
     * POST
     * /members
     * */
    @PostMapping("")
    public ApiResponse<Void> signup(@RequestBody @Valid SignupRequest request) {

        memberService.signup(request);

        return ApiResponse.of(
                SIGNUP
        );
    }

    /**
     * 로그인
     * POST
     * /member/login
     * */
    @PostMapping("/login")
    public ApiResponse<HttpHeaders> login(@RequestBody @Valid LoginRequest request) {

        return ApiResponse.of(
                memberService.login(request),
                LOGIN
        );
    }

    /**
     * JWT 재발급
     * GET
     * /members/reissue
     * 재발급 로직 :
     * access token 401 만료 응답 -> 클라이언트가 refresh token 전송 -> 서버에서 refresh token 검증 -> 새로운 JWT 응답
     * */
    @GetMapping("/reissue")
    public ApiResponse<HttpHeaders> reissueJwt(@RequestHeader("Cookie") String refreshToken) {

        return ApiResponse.of(
                memberService.reissueJwt(refreshToken),
                REISSUE_JWT
        );
    }

    /**
     * 회원 리스트  조회
     * GET
     * /members
     * */
    @GetMapping("")
    public ApiResponse<Page<GetMembersResponse>> getMembers(@PageableDefault(size = 30, page = 0) Pageable pageable) {

        return ApiResponse.of(
                memberService.getMembers(pageable, LocalDate.now()),
                GET_MEMBERS
        );
    }

}
