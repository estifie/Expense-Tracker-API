package com.estifie.expensetracker.response.expense;

import com.estifie.expensetracker.model.Expense;
import org.springframework.data.domain.Page;

import java.util.List;

public record ExpensesResponse(List<ExpenseResponse> expenses) {

    public static ExpensesResponse fromExpenses(List<ExpenseResponse> expenses) {
        return new ExpensesResponse(expenses);
    }

    public static ExpensesResponse fromPaginatedExpenses(Page<Expense> expenses) {
        List<Expense> expenseList = expenses.getContent();

        return new ExpensesResponse(expenseList.stream()
                .map(ExpenseResponse::fromExpense)
                .toList());
    }

    public List<ExpenseResponse> getExpenses() {
        return expenses;
    }
}
