package com.light.backend.member.domain;

import com.light.backend.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Table(name = "member")
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

    //내가 생성한 계정
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    private List<Member> createdMembers;

    @Builder
    private Member(String id, String password, MemberRole role, Member createdBy) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.createdBy = createdBy;
    }

    public static Member create(String id, String password, MemberRole role, Member createdBy) {
        return Member.builder()
                .id(id)
                .password(password)
                .role(role)
                .createdBy(createdBy)
                .build();
    }

    public void updateRefreshTokenValue(String refreshTokenValue) {
        this.refreshTokenValue = refreshTokenValue;
    }
}
