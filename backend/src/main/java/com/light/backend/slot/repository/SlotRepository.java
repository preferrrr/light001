package com.light.backend.slot.repository;

import com.light.backend.slot.domain.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long>, CustomSlotRepository {
}
