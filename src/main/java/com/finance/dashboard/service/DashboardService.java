package com.finance.dashboard.service;

import com.finance.dashboard.dto.response.DashboardResponse;
import com.finance.dashboard.dto.response.DashboardResponse.MonthlySummaryResponse;
import com.finance.dashboard.dto.response.FinancialRecordResponse;
import com.finance.dashboard.entity.FinancialRecord.RecordType;
import com.finance.dashboard.repository.FinancialRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final FinancialRecordRepository recordRepository;
    private final FinancialRecordService recordService;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        BigDecimal totalIncome = getTotalIncome();
        BigDecimal totalExpenses = getTotalExpenses();
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        return DashboardResponse.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(netBalance)
                .categoryTotals(getCategoryTotals())
                .lastFiveTransactions(getLastFiveTransactions())
                .monthlySummary(getMonthlySummary())
                .build();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalIncome() {
        return recordRepository.sumTotalIncome();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalExpenses() {
        return recordRepository.sumTotalExpenses();
    }

    @Transactional(readOnly = true)
    public BigDecimal getNetBalance() {
        return getTotalIncome().subtract(getTotalExpenses());
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getCategoryTotals() {
        List<Object[]> results = recordRepository.findCategoryTotals();
        Map<String, BigDecimal> totals = new LinkedHashMap<>();
        for (Object[] row : results) {
            String category = (String) row[0];
            BigDecimal total = (BigDecimal) row[1];
            totals.put(category, total);
        }
        return totals;
    }

    @Transactional(readOnly = true)
    public List<FinancialRecordResponse> getLastFiveTransactions() {
        return recordRepository.findLast5Transactions()
                .stream()
                .map(recordService::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MonthlySummaryResponse> getMonthlySummary() {
        List<Object[]> rawResults = recordRepository.findMonthlySummary();

        // Group raw results by year + month
        Map<String, MonthlySummaryBuilder> summaryMap = new LinkedHashMap<>();

        for (Object[] row : rawResults) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            String key = year + "-" + String.format("%02d", month);

RecordType type = row[2] instanceof String ? RecordType.valueOf((String) row[2]) : (RecordType) row[2];
            BigDecimal amount = (BigDecimal) row[3];

            summaryMap.computeIfAbsent(key, k -> new MonthlySummaryBuilder(year, month));

            if (type == RecordType.INCOME) {
                summaryMap.get(key).income = summaryMap.get(key).income.add(amount);
            } else {
                summaryMap.get(key).expenses = summaryMap.get(key).expenses.add(amount);
            }
        }

        return summaryMap.values().stream()
                .map(b -> MonthlySummaryResponse.builder()
                        .year(b.year)
                        .month(b.month)
                        .monthName(Month.of(b.month).name())
                        .income(b.income)
                        .expenses(b.expenses)
                        .net(b.income.subtract(b.expenses))
                        .build())
                .collect(Collectors.toList());
    }

    // Private builder helper class
    private static class MonthlySummaryBuilder {
        int year;
        int month;
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expenses = BigDecimal.ZERO;

        MonthlySummaryBuilder(int year, int month) {
            this.year = year;
            this.month = month;
        }
    }
}
