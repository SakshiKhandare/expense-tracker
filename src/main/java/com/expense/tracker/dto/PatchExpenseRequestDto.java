package com.expense.tracker.dto;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PatchExpenseRequestDto {

    // All fields optional for partial update
    @Positive(message = "amount must be positive")
    private Double amount;

    @Size(max = 500, message = "description can be max 500 characters")
    private String description;

    @Size(max = 100, message = "category can be max 100 characters")
    private String category;

    @PastOrPresent(message = "expenseDate cannot be in the future")
    private LocalDate expenseDate;
}
