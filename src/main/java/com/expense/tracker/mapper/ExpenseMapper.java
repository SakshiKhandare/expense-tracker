package com.expense.tracker.mapper;

import com.expense.tracker.dto.ExpenseRequestDto;
import com.expense.tracker.dto.ExpenseResponseDto;
import com.expense.tracker.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    // Convert DTO → Entity
    public Expense toEntity(ExpenseRequestDto dto){

        if(dto==null)   return null;

        Expense e = new Expense();
        e.setAmount(dto.getAmount());
        e.setDescription(dto.getDescription());
        e.setCategory(dto.getCategory());
        e.setExpenseDate(dto.getExpenseDate());

        return e;
    }

    // Convert Entity → DTO
    public ExpenseResponseDto toDto(Expense entity){

        if(entity==null)    return null;

        ExpenseResponseDto dto = new ExpenseResponseDto();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        dto.setExpenseDate(entity.getExpenseDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}
