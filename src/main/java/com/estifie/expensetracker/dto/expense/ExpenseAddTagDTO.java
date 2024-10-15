package com.estifie.expensetracker.dto.expense;

import jakarta.validation.constraints.NotBlank;

public class ExpenseAddTagDTO {
    @NotBlank
    private String tag;

    public ExpenseAddTagDTO(String tag) {
        this.tag = tag;
    }

    public ExpenseAddTagDTO() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
