package com.light.backend;

import com.light.backend.global.jwt.JwtProvider;
import com.light.backend.global.utils.CurrentMemberGetter;
import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.member.repository.repository.MemberRepository;
import com.light.backend.member.service.MemberService;
import com.light.backend.member.service.MemberServiceSupport;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
public abstract class IntegrationTestSupporter {

    @Autowired
    protected MemberService memberService;

    @Autowired
    protected MemberRepository memberRepository;

    @MockBean
    protected CurrentMemberGetter currentMemberGetter;

    @SpyBean
    protected MemberServiceSupport memberServiceSupport;

    @SpyBean
    protected JwtProvider jwtProvider;

    protected static final String MASTER = "master";
    protected static final String ADMIN = "admin";
    protected static final String MEMBER = "member";
    protected static final String PASSWORD = "password";
    protected static final String ACCESS_TOKEN = "accessToken";
    protected static final String REFRESH_TOKEN = "refreshToken";
    protected static final String REFRESH_TOKEN_VALUE = "VALUE";

    protected Member master;

    @BeforeEach
    void setUp() {
        master = Member.create(MASTER, memberServiceSupport.encryptPassword(PASSWORD), MemberRole.MASTER, null);
        memberRepository.save(master);
    }
}
