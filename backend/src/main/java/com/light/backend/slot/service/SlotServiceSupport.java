package com.light.backend.slot.service;

import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.slot.controller.dto.response.GetDashboardResponse;
import com.light.backend.slot.controller.dto.response.GetSlotsResponse;
import com.light.backend.slot.domain.Slot;
import com.light.backend.slot.exception.*;
import com.light.backend.slot.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SlotServiceSupport {

    private final SlotRepository slotRepository;

    public void save(Slot slot) {
        slotRepository.save(slot);
    }

    public void checkCreateSlotAuthority(MemberRole role) {
        if (!role.equals(MemberRole.ADMIN)) {
            throw new UnauthorizedCreateSlotException();
        }
    }

    public void checkCreatedByAdmin(String createdBy, String adminId) {
        if (!createdBy.equals(adminId)) {
            throw new NotCreatedByAdminException();
        }
    }

    public Slot getSlotById(Long id) {
        return slotRepository.findById(id).orElseThrow(NotFoundSlotException::new);
    }

    public void checkSetSlotDataAuthority(Member currentMember, Member slotOwner) {
        if (!currentMember.getRole().equals(MemberRole.MASTER) && slotOwner.getCreatedBy().getId() != currentMember.getId() && slotOwner.getId() != currentMember.getId()) {
            throw new UnauthorizedSetSlotDataException();
        }
    }

    public void checkQueryString(String type, String value) {
        if ((type == null && value != null) || //타입이 null인데, 값이 null이 아닌 경우
                (type != null && !checkIsValidType(type)) || //타입이 유효하지 않은 경우
                ((type != null && checkIsValidType(type)) && (value == null || value.isBlank())) //타입은 유효한데, 값이 null 또는 공백인 경우
        ) {
            throw new InvalidQueryStringForGetSlotsException();
        }
    }

    private boolean checkIsValidType(String type) {
        return type.equals("id") || type.equals("ownerId") || type.equals("adminId") || type.equals("mid") ||
                type.equals("originMid") || type.equals("rankKeyword") || type.equals("workKeyword") || type.equals("description");
    }

    public Page<GetSlotsResponse> getSlotByTypeAndValue(Member member, String type, String value, Pageable pageable) {
        return slotRepository.findSlotByTypeAndValue(member, type, value, pageable);
    }

    public GetDashboardResponse getDashboard(Member member, LocalDate now) {
        return slotRepository.getDashboard(member, now);
    }

    public void saveSlots(List<Slot> slots) {
        slotRepository.saveAll(slots);
    }

    public void checkDeleteAuthority(MemberRole role) {
        if (!role.equals(MemberRole.MASTER))
            throw new UnauthorizedDeleteSlotException();
    }

    public void deleteSlot(Slot slot) {
        slotRepository.delete(slot);
    }
}
