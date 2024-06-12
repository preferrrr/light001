package com.light.backend.slot.repository;

import com.light.backend.member.domain.Member;
import com.light.backend.slot.controller.dto.response.SearchSlotResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomSlotRepository {

    Page<SearchSlotResponse> findSlotByTypeAndValue(Member member, String type, String value, Pageable pageable);

}
