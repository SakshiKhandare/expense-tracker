package com.expense.tracker.repository;

import com.expense.tracker.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    //Find expenses by category (paged)
    Page<Expense> findByCategory(String category, Pageable pageable);

    //Find expenses between two dates (inclusive) (paged)
    Page<Expense> findByExpenseDateBetween(LocalDate fromDate, LocalDate toDate, Pageable pageable);

    //Find expenses by category and date range (paged)
    Page<Expense> findByCategoryAndExpenseDateBetween(String category, LocalDate fromDate, LocalDate toDate, Pageable pageable);
}
