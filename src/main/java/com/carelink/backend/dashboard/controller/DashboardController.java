package com.carelink.backend.dashboard.controller;

import com.carelink.backend.dashboard.dto.DashboardResponseDto;
import com.carelink.backend.dashboard.service.DashboardService;
import com.carelink.backend.global.config.CustomUserDetails;
import com.carelink.backend.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<BaseResponse<?>> getDashboard(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        DashboardResponseDto dashboard =
                dashboardService.getDashboard(customUserDetails.getId());

        return ResponseEntity.ok()
                .body(BaseResponse.success(
                        "대시보드 정보를 정상적으로 불러왔습니다.",
                        dashboard
                ));
    }
}
