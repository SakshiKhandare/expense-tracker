package com.expense.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

/**
 * Summary returned by GET /api/expenses/summary

 * - totalAmount: sum of amounts in the selected window (0.0 if none)
 * - totalExpenses: count of matching expenses
 * - startDate / endDate: actual window used (nullable if not provided)
 * - amountByCategory: optional breakdown by category
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseSummaryDto {

    private Double totalAmount;
    private Long totalExpense;
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<String, Double> amountByCategory;
}
