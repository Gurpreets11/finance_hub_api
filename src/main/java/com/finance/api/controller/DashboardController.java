package com.finance.api.controller;

import com.finance.api.dto.response.ApiResponse;
import com.finance.api.dto.response.DashboardSummaryResponse;
import com.finance.api.entity.User;
import com.finance.api.service.impl.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * GET /api/v1/dashboard/summary
     * Returns everything needed to render the home screen:
     *   - Net worth, total investments, total loans
     *   - This month's income / expenses / investment totals
     *   - Last 5 recent incomes and expenses
     *   - Top 3 investments by current value
     *   - Upcoming loan EMI payments (next 30 days)
     *
     * Requires: Authorization: Bearer <access_token>
     */
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getSummary(
            @AuthenticationPrincipal User currentUser) {

        DashboardSummaryResponse summary = dashboardService.getSummary(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(summary));
    }
}
