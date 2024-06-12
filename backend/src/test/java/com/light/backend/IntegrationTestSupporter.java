package com.light.backend;

import com.light.backend.global.jwt.JwtProvider;
import com.light.backend.global.utils.CurrentMemberGetter;
import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.member.repository.repository.MemberRepository;
import com.light.backend.member.service.MemberService;
import com.light.backend.member.service.MemberServiceSupport;
import com.light.backend.slot.repository.SlotRepository;
import com.light.backend.slot.service.SlotService;
import com.light.backend.slot.service.SlotServiceSupport;
import org.junit.jupiter.api.AfterEach;
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
    protected SlotService slotService;

    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected SlotRepository slotRepository;

    @MockBean
    protected CurrentMemberGetter currentMemberGetter;

    @SpyBean
    protected MemberServiceSupport memberServiceSupport;
    @SpyBean
    protected SlotServiceSupport slotServiceSupport;

    @SpyBean
    protected JwtProvider jwtProvider;

    protected static final String MASTER = "master";
    protected static final String ADMIN = "admin";
    protected static final String ADMIN_2 = "admin2";
    protected static final String MEMBER = "member";
    protected static final String MEMBER_2 = "member2";
    protected static final String PASSWORD = "password";
    protected static final String ACCESS_TOKEN = "accessToken";
    protected static final String REFRESH_TOKEN = "refreshToken";
    protected static final String REFRESH_TOKEN_VALUE = "VALUE";
    protected static final String RANK_KEYWORD = "rank keyword";
    protected static final String WORK_KEYWORD = "work keyword";
    protected static final String DESCRIPTION = "description";
    protected static final String MID = "1111111111";
    protected static final String MID_2 = "1111111112";
    protected static final String ORIGIN_MID = "2222222222";

    protected Member master;

    @BeforeEach
    void setUp() {
        master = Member.create(MASTER, memberServiceSupport.encryptPassword(PASSWORD), MemberRole.MASTER, null);
        master.updateRefreshTokenValue(REFRESH_TOKEN_VALUE);
        memberRepository.save(master);
    }

    @AfterEach
    void tearDown() {
        slotRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }
}
