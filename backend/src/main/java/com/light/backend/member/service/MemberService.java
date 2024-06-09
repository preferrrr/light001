package com.light.backend.member.service;

import com.light.backend.global.utils.CurrentMemberGetter;
import com.light.backend.member.controller.dto.request.LoginRequest;
import com.light.backend.member.controller.dto.request.SignupRequest;
import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberServiceSupport memberServiceSupport;
    private final CurrentMemberGetter currentMemberGetter;

    @Transactional(readOnly = false)
    public void signup(SignupRequest request) {

        //현재 사용자
        Member currentMember = memberServiceSupport.getMemberById(currentMemberGetter.getCurrentMemberId());

        //admin : member 생성만 가능, member : 생성 불가능
        memberServiceSupport.checkAuthority(currentMember.getRole(), request.getRole());

        //이미 가입한 아이디인지 확인
        memberServiceSupport.checkIdExists(request.getId());

        //비밀번호 암호화
        String encryptedPassword = memberServiceSupport.encryptPassword(request.getPassword());

        //멤버 생성
        Member member = request.toEntity(encryptedPassword, currentMember);

        //멤버 저장
        memberServiceSupport.save(member);
    }

}
