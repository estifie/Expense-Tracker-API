package com.estifie.expensetracker.dto.expense;

import jakarta.validation.constraints.NotBlank;

public class ExpenseRemoveTagDTO {
    @NotBlank
    private String tag;

    public ExpenseRemoveTagDTO(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
