package com.light.backend.member.repository.repository;

import com.light.backend.member.controller.dto.response.GetMembersResponse;
import com.light.backend.member.controller.dto.response.QGetMembersResponse;
import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import com.light.backend.member.domain.QMember;
import com.light.backend.slot.domain.QSlot;
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

import static com.light.backend.member.domain.MemberRole.MASTER;

@Repository
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private QMember member = QMember.member;
    private QSlot slot = QSlot.slot;

    @Override
    public Page<GetMembersResponse> getMembersWithSlotCountById(Member currentMember, LocalDate now, Pageable pageable) {

        List<GetMembersResponse> members =  jpaQueryFactory
                .select(new QGetMembersResponse(
                        member.id,
                        member.createdBy.id,
                        totalSlot(currentMember),
                        runningSlot(currentMember, now),
                        closedSlot(currentMember, now),
                        member.createdAt,
                        member.description
                ))
                .from(member)
                .leftJoin(member.slots, slot)
                .where(memberCondition(currentMember))
                .groupBy(member.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.createdAt.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(member.count())
                .from(member)
                .where(memberCondition(currentMember))
                .fetchOne();

        return new PageImpl<>(
                members,
                pageable,
                count
        );
    }


    private BooleanExpression memberCondition(Member currentMember) {
        if (currentMember.getRole().equals(MASTER))
            return null;
        else
            return member.createdBy.id.eq(currentMember.getId());
    }

    private NumberExpression<Integer> runningSlot(Member currentMember, LocalDate now) {
        if (currentMember.getRole().equals(MemberRole.ADMIN))
            return new CaseBuilder()
                    .when(slot.startAt.loe(now).and(slot.endAt.goe(now)))
                    .then(1)
                    .otherwise(0)
                    .sum();
        else
            return new CaseBuilder()
                    .when(member.role.eq(MemberRole.ADMIN))
                    .then(
                            new CaseBuilder()
                                    .when(slot.startAt.loe(now).and(slot.endAt.goe(now)))
                                    .then(1)
                                    .otherwise(0)
                                    .sum()
                                    .add(
                                            jpaQueryFactory
                                                    .select(new CaseBuilder()
                                                            .when(slot.startAt.loe(now).and(slot.endAt.goe(now)))
                                                            .then(1)
                                                            .otherwise(0)
                                                            .sum())
                                                    .from(slot)
                                                    .where(slot.owner.createdBy.id.eq(member.id))
                                    )
                    )
                    .otherwise(
                            new CaseBuilder()
                                    .when(slot.startAt.loe(now).and(slot.endAt.goe(now)))
                                    .then(1)
                                    .otherwise(0)
                                    .sum()
                    );
    }

    private NumberExpression<Integer> closedSlot(Member currentMember, LocalDate now) {
        if (currentMember.getRole().equals(MemberRole.ADMIN)) //ADMIN인 경우
            return new CaseBuilder()
                    .when(slot.endAt.eq(now.minusDays(1)))
                    .then(1)
                    .otherwise(0)
                    .sum();
        else
            return new CaseBuilder()
                    .when(member.role.eq(MemberRole.ADMIN))
                    .then(
                            new CaseBuilder()
                                    .when(slot.endAt.eq(now.minusDays(1)))
                                    .then(1)
                                    .otherwise(0)
                                    .sum()
                                    .add(
                                            jpaQueryFactory
                                                    .select(new CaseBuilder()
                                                            .when(slot.endAt.eq(now.minusDays(1)))
                                                            .then(1)
                                                            .otherwise(0)
                                                            .sum())
                                                    .from(slot)
                                                    .where(slot.owner.createdBy.id.eq(member.id))
                                    )
                    )
                    .otherwise(
                            new CaseBuilder()
                                    .when(slot.endAt.eq(now.minusDays(1)))
                                    .then(1)
                                    .otherwise(0)
                                    .sum()
                    );
    }

    private NumberExpression<Integer> totalSlot(Member currentMember) {
        if (currentMember.getRole().equals(MemberRole.ADMIN)) //ADMIN인 경우
            return slot.count().intValue();
        else { //MASTER인 경우
            return new CaseBuilder()
                    .when(member.role.eq(MemberRole.ADMIN))
                    .then(slot.count().intValue().add(
                                    jpaQueryFactory
                                            .select(slot.count())
                                            .from(slot)
                                            .where(slot.owner.createdBy.id.eq(member.id)))
                            .intValue()
                    )
                    .otherwise(slot.count().intValue());
        }
    }

}
