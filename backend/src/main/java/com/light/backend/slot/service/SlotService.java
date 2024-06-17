package com.light.backend.slot.service;

import com.light.backend.global.utils.CurrentMemberGetter;
import com.light.backend.member.domain.Member;
import com.light.backend.member.service.MemberServiceSupport;
import com.light.backend.naver.NaverOpenApiService;
import com.light.backend.slot.controller.dto.request.CreateSlotRequest;
import com.light.backend.slot.controller.dto.request.SetSlotDataRequest;
import com.light.backend.slot.controller.dto.response.GetDashboardResponse;
import com.light.backend.slot.controller.dto.response.GetSlotsResponse;
import com.light.backend.slot.domain.Slot;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlotService {

    private final SlotServiceSupport slotServiceSupport;
    private final MemberServiceSupport memberServiceSupport;
    private final CurrentMemberGetter currentMemberGetter;
    private final NaverOpenApiService naverOpenApiService;

    @Transactional(readOnly = false)
    public void createSlot(CreateSlotRequest request, LocalDate now) {

        //생성시켜줄 관리자
        Member admin = memberServiceSupport.getMemberById(currentMemberGetter.getCurrentMemberId());

        //관리자가 맞는지 확인
        slotServiceSupport.checkCreateSlotAuthority(admin.getRole());

        //슬롯 사용할 멤버
        Member member = memberServiceSupport.getMemberById(request.getMemberId());

        //관리자가 생성한 멤버가 맞는지 확인
        slotServiceSupport.checkCreatedByAdmin(member.getCreatedBy().getId(), admin.getId());

        Slot slot = request.toEntity(member, now);

        slotServiceSupport.save(slot);

    }

    @Transactional(readOnly = false)
    public void setSlotData(SetSlotDataRequest request) {

        //현재 멤버
        Member currentMember = memberServiceSupport.getMemberById(currentMemberGetter.getCurrentMemberId());

        //데이터를 저장할 슬롯
        Slot slot = slotServiceSupport.getSlotById(request.getId());

        //슬롯의 데이터를 저장할 수 있는지 권한 확인
        slotServiceSupport.checkSetSlotDataAuthority(currentMember, slot.getOwner());

        //슬롯 데이터 수정
        slot.setData(request);

        //네이버 open api로 잘못된 origin mid인지 확인
        boolean isError = naverOpenApiService.checkIsErrorSlot(request.getWorkKeyword(), request.getOriginMid()); //네이버 open api 호출

        //error state default에서 변경
        slot.updateErrorState(isError);

    }

    public Page<GetSlotsResponse> getSlots(String type, String value, Pageable pageable) {

        //현재 멤버
        Member currentMember = memberServiceSupport.getMemberById(currentMemberGetter.getCurrentMemberId());

        //유효한 쿼리스트링 확인
        slotServiceSupport.checkQueryString(type, value);

        //현재 멤버가 마스터면 전부 다, 어드민이면 자기가 관리하고 있는거만, 일반이면 자기꺼만
        return slotServiceSupport.getSlotByTypeAndValue(currentMember, type, value, pageable);
    }

    public GetDashboardResponse getDashboard(LocalDate now) {

        //현재 멤버
        Member currentMember = memberServiceSupport.getMemberById(currentMemberGetter.getCurrentMemberId());

        //대시 보드로 슬롯 현황 통계 조회
        return slotServiceSupport.getDashboard(currentMember, now);
    }

}