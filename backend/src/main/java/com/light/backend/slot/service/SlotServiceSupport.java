package com.light.backend.slot.service;

import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.slot.domain.Slot;
import com.light.backend.slot.exception.NotCreatedByAdminException;
import com.light.backend.slot.exception.NotFoundSlotException;
import com.light.backend.slot.exception.UnauthorizedCreateSlotException;
import com.light.backend.slot.exception.UnauthorizedSetSlotDataException;
import com.light.backend.slot.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


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
}
