package com.expense.tracker.service.impl;

import com.expense.tracker.dto.ExpenseRequestDto;
import com.expense.tracker.dto.ExpenseResponseDto;
import com.expense.tracker.dto.ExpenseSummaryDto;
import com.expense.tracker.dto.PatchExpenseRequestDto;
import com.expense.tracker.entity.Expense;
import com.expense.tracker.exception.ResourceNotFoundException;
import com.expense.tracker.mapper.ExpenseMapper;
import com.expense.tracker.repository.ExpenseRepository;
import com.expense.tracker.service.ExpenseService;
import jdk.jfr.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto dto) {

        Expense existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: "+id));

        existing.setAmount(dto.getAmount());
        existing.setDescription(dto.getDescription());
        existing.setCategory(dto.getCategory());
        existing.setExpenseDate(dto.getExpenseDate());

        // @PreUpdate will automatically set updatedAt

        Expense saved = repository.save(existing);

        return mapper.toDto(saved);
    }

    @Override
    public ExpenseResponseDto partiallyUpdateExpense(Long id, PatchExpenseRequestDto dto) {

        Expense existing = repository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Expense not found with id: "+id));

        if(dto.getAmount() != null){
            existing.setAmount(dto.getAmount());
        }

        if(dto.getDescription() != null){
            existing.setDescription(dto.getDescription());
        }

        if(dto.getCategory() != null){
            existing.setCategory(dto.getCategory());
        }

        if(dto.getExpenseDate() != null){
            existing.setExpenseDate(dto.getExpenseDate());
        }

        Expense saved = repository.save(existing);

        return mapper.toDto(saved);
    }

    @Override
    public void deleteExpense(Long id) {

        Expense existing = repository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Expense not found with id: "+id));

        repository.deleteById(id);
    }

    @Override
    public ExpenseSummaryDto getSummary(LocalDate fromDate, LocalDate toDate, String category, boolean includeCategoryBreakdown) {

        List<Expense> all = repository.findAll();

        Stream<Expense> filtered = all.stream();

        if(category != null && !category.isBlank())
            filtered = filtered.filter(e -> category.equals(e.getCategory()));

        if(fromDate != null)
            filtered = filtered.filter(e -> !e.getExpenseDate().isBefore(fromDate));

        if(toDate != null)
            filtered = filtered.filter(e -> !e.getExpenseDate().isAfter(toDate));

        List<Expense> list = filtered.collect(Collectors.toList());

        double totalAmount = list.stream()
                .mapToDouble(e -> e.getAmount() == null ? 0.0 : e.getAmount())
                .sum();

        long totalExpenses = list.size();

        // Determine actual start/end used in response: if user provided, use those; else derive from data
        LocalDate actualStart = fromDate;
        LocalDate actualEnd = toDate;

        if(actualStart == null && !list.isEmpty())
            actualStart = list.stream().map(Expense::getExpenseDate).min(LocalDate::compareTo).orElse(null);

        if(actualEnd == null && !list.isEmpty())
            actualEnd = list.stream().map(Expense::getExpenseDate).max(LocalDate::compareTo).orElse(null);

        Map<String, Double> breakdown = null;
        if(includeCategoryBreakdown){
            breakdown = list.stream()
                    .collect(Collectors.groupingBy(
                            Expense::getCategory,
                            Collectors.summingDouble(e -> e.getAmount() == null ? 0.0 : e.getAmount())
                    ));
        }

        ExpenseSummaryDto dto = new ExpenseSummaryDto();
        dto.setTotalAmount(totalAmount);
        dto.setTotalExpense(totalExpenses);
        dto.setStartDate(actualStart);
        dto.setEndDate(actualEnd);
        dto.setAmountByCategory(breakdown);

        return dto;

        /*
        This method generates a summary of expenses after applying optional filters.
        It loads all expenses, applies category/date filters, and computes:
            totalAmount → sum of amounts after filtering
            totalExpenses → number of matching records
            startDate / endDate → user-provided values, or derived from the filtered data
            amountByCategory → optional map of category → total amount (only when includeCategoryBreakdown = true)

        The method returns an ExpenseSummaryDto containing these aggregated results, providing a simple way to analyze spending patterns over a selected time range or category.
         */
    }

}
