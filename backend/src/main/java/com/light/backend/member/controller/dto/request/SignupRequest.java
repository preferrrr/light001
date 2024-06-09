package com.light.backend.member.controller.dto.request;

import com.light.backend.member.domain.Member;
import com.light.backend.member.domain.MemberRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupRequest {

    @NotBlank(message = "아이디은 null 또는 공백일 수 없습니다.")
    @Size(min = 4, max = 20, message = "아이디는 4 ~ 20자여야 합니다.")
    private String id;

    @NotBlank(message = "비밀번호는 null 또는 공백일 수 없습니다.")
    @Size(min = 4, max = 20, message = "비밀번호는 4 ~ 20자여야 합니다.")
    private String password;

    @NotNull(message = "권한은 null 또는 공백일 수 없습니다.")
    private MemberRole role;

    @Builder
    public SignupRequest(String id, String password, MemberRole role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public Member toEntity(String password, Member member) {
        return Member.create(
                this.id,
                password,
                this.role,
                member
        );
    }
}
