package com.estifie.expensetracker.repository;

import com.estifie.expensetracker.model.Expense;
import com.estifie.expensetracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
    Page<Expense> findByUser(User user, Pageable pageable);

    Page<Expense> findByUserAndDeletedAtIsNull(User user, Pageable pageable);

    Optional<Expense> findByIdAndDeletedAtIsNull(String id);

    Page<Expense> findAllByDeletedAtIsNull(Pageable pageable);
}
