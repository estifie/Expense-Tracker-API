package com.estifie.expensetracker.service;

import com.estifie.expensetracker.dto.expense.ExpenseCreateDTO;
import com.estifie.expensetracker.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseService {
    void create(String username, ExpenseCreateDTO expenseCreateDTO);
    
    void delete(String id);

    Page<Expense> findByUsername(String username, Pageable pageable);

    Page<Expense> findAll(Pageable pageable);
}
