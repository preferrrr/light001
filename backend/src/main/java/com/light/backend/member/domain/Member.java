package com.light.backend.member.domain;

import com.light.backend.global.BaseEntity;
import com.light.backend.slot.domain.Slot;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Table(name = "member", indexes = {
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert @DynamicUpdate
public class Member extends BaseEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String refreshTokenValue;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    //나를 생성해준 계정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Member createdBy;

    private String description;

    //내가 생성한 계정
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    private List<Member> createdMembers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Slot> slots;

    @Builder
    private Member(String id, String password, MemberRole role, Member createdBy, String description) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.createdBy = createdBy;
        this.description = description;
    }

    public static Member create(String id, String password, MemberRole role, Member createdBy, String description) {
        return Member.builder()
                .id(id)
                .password(password)
                .role(role)
                .createdBy(createdBy)
                .description(description)
                .build();
    }

    public void updateRefreshTokenValue(String refreshTokenValue) {
        this.refreshTokenValue = refreshTokenValue;
    }
}
