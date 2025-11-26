package com.expense.tracker.controller;

import com.expense.tracker.dto.*;
import com.expense.tracker.repository.ExpenseRepository;
import com.expense.tracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> getExpenseById(@PathVariable("id") Long id){
        ExpenseResponseDto dto = expenseService.getExpenseById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseResponseDto>> listExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @PageableDefault(page = 0, size = 20) Pageable pageable
    ) {
        Page<ExpenseResponseDto> page = expenseService.getExpenses(category, fromDate, toDate, pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(
            @PathVariable("id") Long id,
            @Valid @RequestBody ExpenseRequestDto expenseRequestDto
    ){
        ExpenseResponseDto updated = expenseService.updateExpense(id, expenseRequestDto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> partiallyUpdateExpense(
            @PathVariable("id") Long id,
            @Valid @RequestBody PatchExpenseRequestDto patchExpenseRequestDto
    ){
        ExpenseResponseDto updated = expenseService.partiallyUpdateExpense(id, patchExpenseRequestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") Long id){
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<ExpenseSummaryDto> getSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "false") boolean includeCategoryBreakdown
    ){
        ExpenseSummaryDto summary = expenseService.getSummary(fromDate, toDate, category, includeCategoryBreakdown);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/monthly")
    public ResponseEntity<MonthlySummaryDto> getMonthlySummary(
            @RequestParam(required = false, defaultValue = "false") boolean includeCategoryBreakdown
    ){
        MonthlySummaryDto summary = expenseService.getMonthlySummary(includeCategoryBreakdown);
        return ResponseEntity.ok(summary);
    }
}
