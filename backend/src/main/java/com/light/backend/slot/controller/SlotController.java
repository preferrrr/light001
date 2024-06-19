package com.light.backend.slot.controller;

import com.light.backend.global.response.ApiResponse;
import com.light.backend.slot.controller.dto.request.CreateSlotRequest;
import com.light.backend.slot.controller.dto.request.SetSlotDataRequest;
import com.light.backend.slot.controller.dto.response.GetDashboardResponse;
import com.light.backend.slot.controller.dto.response.GetSlotsResponse;
import com.light.backend.slot.service.SlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.light.backend.global.response.code.SlotResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slots")
public class SlotController {

    private final SlotService slotService;

    /**
     * 슬롯 생성
     * POST
     * /slots
     */
    @PostMapping("")
    public ApiResponse<Void> createSlot(@RequestBody @Valid CreateSlotRequest request) {

        slotService.createSlot(request, LocalDate.now());

        return ApiResponse.of(
                CREATE_SLOT
        );
    }

    /**
     * 슬롯 데이터 저장
     * PATCH
     * /slots
     */
    @PatchMapping("")
    public ApiResponse<Void> setSlotData(@RequestBody @Valid SetSlotDataRequest request) {

        slotService.setSlotData(request);

        return ApiResponse.of(
                SET_SLOT_DATA
        );
    }

    /**
     * 슬롯 리스트 조회
     * GET
     * /slots
     */
    @GetMapping("")
    public ApiResponse<Page<GetSlotsResponse>> getSlots(@RequestParam(name = "type", required = false) String type,
                                                        @RequestParam(name = "value", required = false) String value,
                                                        @PageableDefault(size = 30, page = 0) Pageable pageable) {

        return ApiResponse.of(
                slotService.getSlots(type, value, pageable),
                GET_SLOTS
        );
    }

    /**
     * 대시보드 조회
     * GET
     * /slots/dashboard
     */
    @GetMapping("/dashboard")
    public ApiResponse<GetDashboardResponse> getDashboard() {

        return ApiResponse.of(
                slotService.getDashboard(LocalDate.now()),
                GET_DASHBOARD
        );
    }

    /**
     * 슬롯 삭제
     * DELETE
     * /slots?id=
     */
    @DeleteMapping("")
    public ApiResponse<Void> deleteSlot(@RequestParam(name = "id", required = true) Long id) {

        slotService.deleteSlot(id);

        return ApiResponse.of(
                DELETE_SLOT
        );
    }
}
