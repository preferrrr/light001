package com.light.backend.slot.repository;

import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.member.domain.QMember;
import com.light.backend.slot.controller.dto.response.SearchSlotResponse;
import com.light.backend.slot.domain.QSlot;
import com.light.backend.slot.domain.Slot;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.light.backend.member.domain.MemberRole.*;

@Repository
@RequiredArgsConstructor
public class CustomSlotRepositoryImpl implements CustomSlotRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private QSlot slot = QSlot.slot;

    @Override
    public Page<SearchSlotResponse> findSlotByTypeAndValue(Member currentMember, String type, String value, Pageable pageable) {

        List<Slot> slots = jpaQueryFactory
                .selectFrom(slot)
                .where(typeAndValueCondition(type, value),
                        memberCondition(currentMember)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(slot.id.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(slot.count())
                .from(slot)
                .where(typeAndValueCondition(type, value),
                        memberCondition(currentMember)
                )
                .fetchOne();

        return new PageImpl<>(
                slots.stream()
                        .map(s -> SearchSlotResponse.of(s))
                        .toList(),
                pageable,
                count
        );
    }

    private BooleanExpression memberCondition(Member member) {
        if (member.getRole().equals(MASTER))
            return null;
        else if (member.getRole().equals(ADMIN))
            return slot.owner.id.eq(member.getId())
                    .or(slot.owner.createdBy.id.eq(member.getId()));
        else
            return slot.owner.id.eq(member.getId());
    }

    private BooleanExpression typeAndValueCondition(String type, String value) {

        if (type == null)
            return null;

        switch (type) {
            case "id":
                return slot.id.eq(Long.parseLong(value));
            case "ownerId":
                return slot.owner.id.eq(value);
            case "adminId":
                return slot.owner.createdBy.id.eq(value);
            case "mid":
                return slot.mid.eq(value);
            case "originMid":
                return slot.originMid.eq(value);
            case "rankKeyword":
                return slot.rankKeyword.eq(value);
            case "workKeyword":
                return slot.workKeyword.eq(value);
            case "description":
                return slot.description.eq(value);
            default:
                return null;
        }
    }


}
