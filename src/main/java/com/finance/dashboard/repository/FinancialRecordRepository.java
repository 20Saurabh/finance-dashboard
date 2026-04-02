package com.finance.dashboard.repository;

import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.FinancialRecord.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    // --- Filtering ---

    @Query("""
            SELECT r FROM FinancialRecord r
            WHERE r.deleted = false
              AND (:startDate IS NULL OR r.date >= :startDate)
              AND (:endDate IS NULL OR r.date <= :endDate)
              AND (:category IS NULL OR LOWER(r.category) LIKE LOWER(CONCAT('%', :category, '%')))
              AND (:type IS NULL OR r.type = :type)
            ORDER BY r.date DESC
            """)
    Page<FinancialRecord> findWithFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("category") String category,
            @Param("type") RecordType type,
            Pageable pageable
    );

    // --- Dashboard aggregations ---

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r WHERE r.type = 'INCOME' AND r.deleted = false")
    BigDecimal sumTotalIncome();

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r WHERE r.type = 'EXPENSE' AND r.deleted = false")
    BigDecimal sumTotalExpenses();

    @Query("""
            SELECT r.category, SUM(r.amount)
            FROM FinancialRecord r
            WHERE r.deleted = false
            GROUP BY r.category
            ORDER BY SUM(r.amount) DESC
            """)
    List<Object[]> findCategoryTotals();

    @Query("""
            SELECT r FROM FinancialRecord r
            WHERE r.deleted = false
            ORDER BY r.date DESC, r.createdAt DESC
            LIMIT 5
            """)
    List<FinancialRecord> findLast5Transactions();

    @Query("""
            SELECT YEAR(r.date), MONTH(r.date), r.type, SUM(r.amount)
            FROM FinancialRecord r
            WHERE r.deleted = false
            GROUP BY YEAR(r.date), MONTH(r.date), r.type
            ORDER BY YEAR(r.date) DESC, MONTH(r.date) DESC
            """)
    List<Object[]> findMonthlySummary();

    // --- Search ---

    @Query("""
            SELECT r FROM FinancialRecord r
            WHERE r.deleted = false
              AND (LOWER(r.category) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(r.notes) LIKE LOWER(CONCAT('%', :keyword, '%')))
            ORDER BY r.date DESC
            """)
    Page<FinancialRecord> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
