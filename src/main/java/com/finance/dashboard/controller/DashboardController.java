package com.finance.dashboard.controller;

import com.finance.dashboard.dto.response.ApiResponse;
import com.finance.dashboard.dto.response.DashboardResponse;
import com.finance.dashboard.dto.response.DashboardResponse.MonthlySummaryResponse;
import com.finance.dashboard.dto.response.FinancialRecordResponse;
import com.finance.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {
        DashboardResponse dashboard = dashboardService.getDashboard();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard data retrieved"));
    }

    @GetMapping("/income")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalIncome() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getTotalIncome(), "Total income retrieved"));
    }

    @GetMapping("/expenses")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalExpenses() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getTotalExpenses(), "Total expenses retrieved"));
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<BigDecimal>> getNetBalance() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getNetBalance(), "Net balance retrieved"));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> getCategoryTotals() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getCategoryTotals(), "Category totals retrieved"));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<FinancialRecordResponse>>> getLastFiveTransactions() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getLastFiveTransactions(), "Recent transactions retrieved"));
    }

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<List<MonthlySummaryResponse>>> getMonthlySummary() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getMonthlySummary(), "Monthly summary retrieved"));
    }
}
