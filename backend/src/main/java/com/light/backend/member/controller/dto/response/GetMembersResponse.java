package com.light.backend.member.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetMembersResponse {
    private String member;
    private String admin;
    private int total;
    private int running;
    private int closed;
    private LocalDateTime createdAt;

    @QueryProjection
    public GetMembersResponse(String member, String admin, int total, int running, int closed, LocalDateTime createdAt) {
        this.member = member;
        this.admin = admin;
        this.total = total;
        this.running = running;
        this.closed = closed;
        this.createdAt = createdAt;
    }
}
