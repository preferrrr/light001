package com.light.backend.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.light.backend.member.exception.InvalidMemberRoleException;

public enum MemberRole {
    MASTER("master", "마스터 계정"),
    ADMIN("admin", "관리자 계정"),
    MEMBER("member", "일반 계정");

    private String value;
    private String description;

    MemberRole(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonCreator
    public static MemberRole from(String value) {
        for (MemberRole memberRole : MemberRole.values()) {
            if (memberRole.value.equals(value)) {
                return memberRole;
            }
        }

        throw new InvalidMemberRoleException();
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
