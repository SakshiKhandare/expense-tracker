package com.expense.tracker.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseRequestDto {

    @NotNull(message = "amount is required")
    @Positive(message = "amount must be positive")
    private Double amount;

    @Size(max = 500, message = "description can be max 500 characters")
    private String description;

    @NotBlank(message = "category is required")
    @Size(max = 100, message = "category can be max 100 characters")
    private String category;

    @NotNull(message = "expenseDate is required")
    private LocalDate expenseDate;
}