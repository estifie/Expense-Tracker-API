package com.estifie.expensetracker.dto.expense;

import com.estifie.expensetracker.model.Expense;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class ExpenseCreateDTO {
    private String currencyCode;

    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    private String note;

    public ExpenseCreateDTO(String currencyCode, BigDecimal amount, String note) {
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.note = note;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Expense toExpense() {
        return new Expense(amount, currencyCode, note);
    }
}
