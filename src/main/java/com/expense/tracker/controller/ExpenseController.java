package com.expense.tracker.controller;

import com.expense.tracker.dto.ExpenseRequestDto;
import com.expense.tracker.dto.ExpenseResponseDto;
import com.expense.tracker.repository.ExpenseRepository;
import com.expense.tracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> createExpense(
            @Valid @RequestBody ExpenseRequestDto expenseRequestDto){

        ExpenseResponseDto response = expenseService.createExpense(expenseRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
