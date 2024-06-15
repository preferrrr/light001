package com.light.backend.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequest {

    @NotBlank(message = "아이디는 null 또는 공백일 수 없습니다.")
    private String id;
    @NotBlank(message = "비밀번호는 null 또는 공백일 수 없습니다.")
    private String password;

    @Builder
    public LoginRequest(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
