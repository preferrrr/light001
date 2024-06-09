package com.light.backend;

import com.light.backend.global.utils.CurrentMemberGetter;
import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.member.repository.repository.MemberRepository;
import com.light.backend.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public abstract class IntegrationTestSupporter {

    @Autowired
    protected MemberService memberService;

    @Autowired
    protected MemberRepository memberRepository;

    @MockBean
    protected CurrentMemberGetter currentMemberGetter;

    protected static final String MASTER = "master";
    protected static final String ADMIN = "admin";
    protected static final String MEMBER = "member";
    protected static final String PASSWORD = "password";

    protected Member master;

    @BeforeEach
    void setUp() {
        master = Member.create(MASTER, PASSWORD, MemberRole.MASTER, null);
        memberRepository.save(master);
    }
}
