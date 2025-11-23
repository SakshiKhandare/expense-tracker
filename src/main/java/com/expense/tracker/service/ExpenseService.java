package com.expense.tracker.service;

import com.expense.tracker.dto.ExpenseRequestDto;
import com.expense.tracker.dto.ExpenseResponseDto;
import org.springframework.stereotype.Service;

public interface ExpenseService {

    ExpenseResponseDto createExpense(ExpenseRequestDto dto);

    ExpenseResponseDto getExpenseById(Long id);
}
