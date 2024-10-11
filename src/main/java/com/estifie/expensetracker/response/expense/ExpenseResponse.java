package com.estifie.expensetracker.response.expense;

import com.estifie.expensetracker.model.Expense;

public record ExpenseResponse(String id, String currencyCode, String amount,
                              String note) {

    public static ExpenseResponse fromExpense(Expense expense) {
        return new ExpenseResponse(expense.getId(), expense.getCurrencyCode(), expense.getAmount()
                .toString(), expense.getNote());
    }
}
