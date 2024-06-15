package com.light.backend.slot.repository;

import com.light.backend.member.domain.Member;
import com.light.backend.slot.controller.dto.response.GetDashboardResponse;
import com.light.backend.slot.controller.dto.response.QGetDashboardResponse;
import com.light.backend.slot.controller.dto.response.SearchSlotResponse;
import com.light.backend.slot.domain.QSlot;
import com.light.backend.slot.domain.Slot;
import com.light.backend.slot.domain.SlotErrorState;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.light.backend.member.domain.MemberRole.ADMIN;
import static com.light.backend.member.domain.MemberRole.MASTER;

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

    @Override
    public GetDashboardResponse getDashboard(Member currentMember, LocalDate now) {

        return jpaQueryFactory
                .select(new QGetDashboardResponse(
                                slot.count().intValue(),
                                waitingSlot(),
                                runningSlot(now),
                                expiringSlot(now),
                                closedSlot(now),
                                errorSlot(now)
                        )
                )
                .from(slot)
                .where(memberCondition(currentMember))
                .fetchOne();
    }

    private NumberExpression<Integer> waitingSlot() {
        return new CaseBuilder()
                .when(slot.mid.isNull())
                .then(1)
                .otherwise(0)
                .sum();
    }

    private NumberExpression<Integer> runningSlot(LocalDate now) {
        return new CaseBuilder()
                .when(slot.startAt.loe(now).and(slot.endAt.goe(now)))
                .then(1)
                .otherwise(0)
                .sum();
    }

    private NumberExpression<Integer> closedSlot(LocalDate now) {
        return new CaseBuilder()
                .when(slot.endAt.eq(now.minusDays(1)))
                .then(1)
                .otherwise(0)
                .sum();
    }

    private NumberExpression<Integer> expiringSlot(LocalDate now) {
        return new CaseBuilder()
                .when(slot.endAt.between(now, now.plusDays(2)))
                .then(1)
                .otherwise(0)
                .sum();
    }

    private NumberExpression<Integer> errorSlot(LocalDate now) {
        return new CaseBuilder()
                .when(slot.startAt.loe(now).and(slot.endAt.goe(now)).and(slot.slotErrorState.eq(SlotErrorState.Y)))
                .then(1)
                .otherwise(0)
                .sum();
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
