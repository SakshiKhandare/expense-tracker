package com.expense.tracker.service.impl;

import com.expense.tracker.dto.ExpenseRequestDto;
import com.expense.tracker.dto.ExpenseResponseDto;
import com.expense.tracker.entity.Expense;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.mapper.ExpenseMapper;
import com.expense.tracker.repository.ExpenseRepository;
import com.expense.tracker.service.ExpenseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository repository;
    private final ExpenseMapper mapper;

    public ExpenseServiceImpl(ExpenseRepository repository, ExpenseMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ExpenseResponseDto createExpense(ExpenseRequestDto dto) {
        // Convert DTO → Entity
        Expense entity = mapper.toEntity(dto);

        // Save new expense
        Expense saved = repository.save(entity);

        // Return created expense as DTO
        return mapper.toDto(saved);
    }

    @Override
    public ExpenseResponseDto getExpenseById(Long id) {
        Expense expense = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        return mapper.toDto(expense);
    }

    @Override
    public Page<ExpenseResponseDto> getExpenses(String category, LocalDate fromDate, LocalDate toDate, Pageable pageable) {

        // If only one date is provided, treat it as exact date
        if(fromDate != null && toDate == null){
            toDate = fromDate;
        }
        if(fromDate == null && toDate != null){
            fromDate = toDate;
        }

        Page<Expense> page;

        // All three filters (category + date range)
        if(category != null && fromDate != null && toDate != null){
            page = repository.findByCategoryAndExpenseDateBetween(category, fromDate, toDate, pageable);
        }
        // Only category filter
        else if (category != null) {
            page = repository.findByCategory(category, pageable);
        }
        // Only date filter
        else if (fromDate != null && toDate != null) {
            page = repository.findByExpenseDateBetween(fromDate, toDate, pageable);
        }
        // No filters
        else {
            page = repository.findAll(pageable);
        }

        return page.map(mapper::toDto);
    }

}
