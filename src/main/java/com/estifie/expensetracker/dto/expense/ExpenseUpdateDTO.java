package com.estifie.expensetracker.dto.expense;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Data transfer object for updating an expense")
public class ExpenseUpdateDTO {
    @Schema(description = "The currency code for the expense (e.g., USD, EUR)", example = "USD", defaultValue = "USD")
    private String currencyCode;

    @NotNull
    @PositiveOrZero
    @Schema(description = "The amount of the expense", example = "50.00", minimum = "0")
    private BigDecimal amount;

    @Schema(description = "Optional note about the expense", example = "Grocery shopping", maxLength = 1024)
    private String note;

    public ExpenseUpdateDTO(String currencyCode, BigDecimal amount, String note) {
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.note = note;
    }

    public ExpenseUpdateDTO() {
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
} 