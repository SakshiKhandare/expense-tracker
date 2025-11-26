package com.expense.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class MonthlySummaryDto {

    private List<MonthlySummaryItem> items;

    /**
     * Custom constructor needed because Lombok's all-args constructor
     * does not match the expected (List<MonthlySummaryItem>) constructor.
     */
    public MonthlySummaryDto(List<MonthlySummaryItem> items, boolean dummy) {
        this.items = items;
    }

    /**
     * Clean constructor for `return new MonthlySummaryDto(items)`
     */
    public MonthlySummaryDto(List<MonthlySummaryItem> items) {
        this.items = items;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlySummaryItem {

        /**
         * Month identifier in format "YYYY-MM"
         * e.g., "2025-11"
         */
        private String yearMonth;

        /**
         * Total amount spent in that month.
         */
        private Double totalAmount;

        /**
         * Total number of expenses in that month.
         */
        private Long totalExpenses;

        /**
         * Optional breakdown: category → total amount.
         */
        private Map<String, Double> amountByCategory;
    }
}
