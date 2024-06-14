package com.light.backend.slot.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetDashboardResponse {
    private int total;
    private int waiting;
    private int running;
    private int expiring;
    private int closed;
    private int error;

    @Builder
    @QueryProjection
    public GetDashboardResponse(int total, int waiting, int running, int expiring, int closed, int error) {
        this.total = total;
        this.waiting = waiting;
        this.running = running;
        this.expiring = expiring;
        this.closed = closed;
        this.error = error;
    }

    public static GetDashboardResponse of(int total, int waiting, int running, int expiring, int closed, int error) {
        return GetDashboardResponse.builder()
                .total(total)
                .waiting(waiting)
                .running(running)
                .expiring(expiring)
                .closed(closed)
                .error(error)
                .build();
    }
}
