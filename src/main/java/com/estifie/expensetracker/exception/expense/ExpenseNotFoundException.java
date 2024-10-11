package com.estifie.expensetracker.exception.expense;

import com.estifie.expensetracker.exception.global.NotFoundException;

public class ExpenseNotFoundException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "Expense not found";

    public ExpenseNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
