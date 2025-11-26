package com.expense.tracker.service;

import com.expense.tracker.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ExpenseService {

    ExpenseResponseDto createExpense(ExpenseRequestDto dto);

    ExpenseResponseDto getExpenseById(Long id);

    Page<ExpenseResponseDto> getExpenses(String category, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto dto);

    ExpenseResponseDto partiallyUpdateExpense(Long id, PatchExpenseRequestDto dto);

    void deleteExpense(Long id);

    ExpenseSummaryDto getSummary(LocalDate fromDate, LocalDate toDate, String category, boolean includeCategoryBreakdown);

    MonthlySummaryDto getMonthlySummary(boolean includeCategoryBreakdown);
}
