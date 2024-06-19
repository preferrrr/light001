package com.light.backend.member.controller.dto.request;

import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupRequest {

    @NotBlank(message = "아이디은 null 또는 공백일 수 없습니다.")
    @Size(min = 4, max = 20, message = "아이디는 4 ~ 20자여야 합니다.")
    private String id;

    @NotBlank(message = "비밀번호는 null 또는 공백일 수 없습니다.")
    @Size(min = 4, max = 20, message = "비밀번호는 4 ~ 20자여야 합니다.")
    private String password;

    private String description;

    @Builder
    public SignupRequest(String id, String password, MemberRole role, String description) {
        this.id = id;
        this.password = password;
        this.description = description;
    }

    public Member toEntity(String password, Member member) {
        MemberRole role = MemberRole.MEMBER;

        if (member.getRole().equals(MemberRole.MASTER))
            role = MemberRole.ADMIN;

        return Member.create(
                this.id,
                password,
                role,
                member,
                description
        );
    }
}
