package com.expense.tracker.repository;

import com.expense.tracker.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    //Find expenses by category (paged)
    Page<Expense> findByCategory(String category, Pageable pageable);

    //Find expenses between two dates (inclusive) (paged)
    Page<Expense> findByExpenseDateBetween(LocalDate fromDate, LocalDate toDate, Pageable pageable);

    //Find expenses by category and date range (paged)
    Page<Expense> findByCategoryAndExpenseDateBetween(String category, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    /**
     * Sum amounts with optional filters. If a parameter is null, that filter is ignored.
     */
    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE (:category IS NULL OR e.category = :category)
          AND (:fromDate IS NULL OR e.expenseDate >= :fromDate)
          AND (:toDate IS NULL OR e.expenseDate <= :toDate)
        """)
    Double sumAmount(
            @Param("category") String category,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    /**
     * Count matching expenses with optional filters.
     */
    @Query("""
        SELECT COUNT(e)
        FROM Expense e
        WHERE (:category IS NULL OR e.category = :category)
          AND (:fromDate IS NULL OR e.expenseDate >= :fromDate)
          AND (:toDate IS NULL OR e.expenseDate <= :toDate)
        """)
    Long countExpenses(
            @Param("category") String category,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    /**
     * Breakdown: returns rows of (category, totalAmount) for matching rows.
     * Note: returns List<Object[]> where each element is [String category, Double totalAmount].
     */
    @Query("""
        SELECT e.category, COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE (:fromDate IS NULL OR e.expenseDate >= :fromDate)
          AND (:toDate IS NULL OR e.expenseDate <= :toDate)
          AND (:category IS NULL OR e.category = :category)
        GROUP BY e.category
        """)
    List<Object[]> sumAmountByCategory(
            @Param("category") String category,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

}
