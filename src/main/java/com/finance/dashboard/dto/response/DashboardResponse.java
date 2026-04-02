package com.finance.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;
    private Map<String, BigDecimal> categoryTotals;
    private List<FinancialRecordResponse> lastFiveTransactions;
    private List<MonthlySummaryResponse> monthlySummary;

    @Data
    @Builder
    public static class MonthlySummaryResponse {
        private int year;
        private int month;
        private String monthName;
        private BigDecimal income;
        private BigDecimal expenses;
        private BigDecimal net;
    }
}
