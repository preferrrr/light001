package com.light.backend.member.repository.repository;

import com.light.backend.member.controller.dto.response.GetMembersResponse;
import com.light.backend.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CustomMemberRepository {

    Page<GetMembersResponse> getMembersWithSlotCountById(Member currentMember, LocalDate now, Pageable pageable);

}
