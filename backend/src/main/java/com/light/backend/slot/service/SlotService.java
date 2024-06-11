package com.light.backend.slot.service;

import com.light.backend.global.utils.CurrentMemberGetter;
import com.light.backend.member.domain.Member;
import com.light.backend.member.service.MemberServiceSupport;
import com.light.backend.slot.controller.dto.request.CreateSlotRequest;
import com.light.backend.slot.controller.dto.request.SetSlotDataRequest;
import com.light.backend.slot.domain.Slot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlotService {

    private final SlotServiceSupport slotServiceSupport;
    private final MemberServiceSupport memberServiceSupport;
    private final CurrentMemberGetter currentMemberGetter;

    @Transactional(readOnly = false)
    public void createSlot(CreateSlotRequest request) {

        //생성시켜줄 관리자
        Member admin = memberServiceSupport.getMemberById(currentMemberGetter.getCurrentMemberId());

        //관리자가 맞는지 확인
        slotServiceSupport.checkCreateSlotAuthority(admin.getRole());

        //슬롯 사용할 멤버
        Member member = memberServiceSupport.getMemberById(request.getMemberId());

        //관리자가 생성한 멤버가 맞는지 확인
        slotServiceSupport.checkCreatedByAdmin(member.getCreatedBy().getId(), admin.getId());

        Slot slot = request.toEntity(member);

        slotServiceSupport.save(slot);

    }

    @Transactional(readOnly = false)
    public void setSlotData(SetSlotDataRequest request) {

        //현재 멤버
        Member currentMember = memberServiceSupport.getMemberById(currentMemberGetter.getCurrentMemberId());

        //데이터를 저장할 슬롯
        Slot slot = slotServiceSupport.getSlotById(request.getId());

        //슬롯의 데이터를 수정할 수 있는지 권한 확인
        slotServiceSupport.checkSetSlotDataAuthority(currentMember, slot.getOwner());

        //슬롯 데이터 수정
        slot.setData(request);

        boolean isError = false; //네이버 open api 호출

        slot.updateErrorState(isError);

    }

}