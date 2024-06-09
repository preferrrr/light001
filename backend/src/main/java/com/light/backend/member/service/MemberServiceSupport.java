package com.light.backend.member.service;

import com.light.backend.global.jwt.JwtProvider;
import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.member.exception.*;
import com.light.backend.member.repository.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.light.backend.member.domain.MemberRole.*;


@Service
@RequiredArgsConstructor
public class MemberServiceSupport {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

}
