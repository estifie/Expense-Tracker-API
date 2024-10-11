package com.estifie.expensetracker.service;

import com.estifie.expensetracker.dto.expense.ExpenseCreateDTO;
import com.estifie.expensetracker.enums.Permission;
import com.estifie.expensetracker.exception.expense.ExpenseNotFoundException;
import com.estifie.expensetracker.exception.user.UserNotFoundException;
import com.estifie.expensetracker.model.Expense;
import com.estifie.expensetracker.model.User;
import com.estifie.expensetracker.repository.ExpenseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserService userService) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    public Optional<Expense> findById(String id, boolean fetchDeleted) {
        return fetchDeleted ? expenseRepository.findById(id) : expenseRepository.findByIdAndDeletedAtIsNull(id);
    }

    public void create(String username, ExpenseCreateDTO expenseCreateDTO) {
        User user = userService.findByUsername(username);
        if (user == null) throw new UserNotFoundException();

        Expense expense = expenseCreateDTO.toExpense();
        expense.setUser(user);

        expenseRepository.save(expense);
    }

    public void delete(String id, boolean hardDelete) {
        Expense expense =
                expenseRepository.findById(id)
                        .orElseThrow(ExpenseNotFoundException::new);

        expense.setDeletedAt(LocalDateTime.now());

        boolean canHardDelete = userService.hasPermission(expense.getUser()
                .getUsername(), Permission.HARD_DELETE_EXPENSE.name());

        if (hardDelete && canHardDelete) {
            expenseRepository.deleteById(id);
        } else {
            expenseRepository.save(expense);
        }
    }

    public Page<Expense> findByUsername(String username, Pageable pageable, boolean fetchDeleted) {
        return fetchDeleted ? expenseRepository.findByUser(userService.findByUsername(username), pageable) : expenseRepository.findByUserAndDeletedAtIsNull(userService.findByUsername(username), pageable);
    }

    public Page<Expense> findAll(Pageable pageable, boolean fetchDeleted) {
        return fetchDeleted ? expenseRepository.findAll(pageable) : expenseRepository.findAllByDeletedAtIsNull(pageable);
    }
}
