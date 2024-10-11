package com.estifie.expensetracker.service;

import com.estifie.expensetracker.dto.expense.ExpenseCreateDTO;
import com.estifie.expensetracker.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ExpenseService {
    Optional<Expense> findById(String id, boolean fetchDeleted);

    void create(String username, ExpenseCreateDTO expenseCreateDTO);

    void delete(String id, boolean hardDelete);

    Page<Expense> findByUsername(String username, Pageable pageable, boolean fetchDeleted);

    Page<Expense> findAll(Pageable pageable, boolean fetchDeleted);
}
