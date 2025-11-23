package com.expense.tracker.service;

import com.expense.tracker.dto.ExpenseRequestDto;
import com.expense.tracker.dto.ExpenseResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

public interface ExpenseService {

    ExpenseResponseDto createExpense(ExpenseRequestDto dto);

    ExpenseResponseDto getExpenseById(Long id);

    Page<ExpenseResponseDto> getExpenses(String category, LocalDate fromDate, LocalDate toDate, Pageable pageable);
}
