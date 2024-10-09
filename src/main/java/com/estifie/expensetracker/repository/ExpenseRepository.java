package com.estifie.expensetracker.repository;

import com.estifie.expensetracker.model.Expense;
import com.estifie.expensetracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
    Page<Expense> findByUser(User user, Pageable pageable);
}
