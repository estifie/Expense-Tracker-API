package com.estifie.expensetracker.service;

import com.estifie.expensetracker.dto.expense.ExpenseCreateDTO;
import com.estifie.expensetracker.dto.expense.ExpenseUpdateDTO;
import com.estifie.expensetracker.enums.Permission;
import com.estifie.expensetracker.exception.expense.ExpenseNotFoundException;
import com.estifie.expensetracker.exception.tag.TagNotFoundException;
import com.estifie.expensetracker.exception.user.UserNotFoundException;
import com.estifie.expensetracker.model.Expense;
import com.estifie.expensetracker.model.Tag;
import com.estifie.expensetracker.model.User;
import com.estifie.expensetracker.repository.ExpenseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Component("expenseService")
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final TagService tagService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserService userService, TagService tagService) {
        this.expenseRepository = expenseRepository;
        this.userService = userService;
        this.tagService = tagService;
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

    public void update(String id, ExpenseUpdateDTO expenseUpdateDTO) {
        Expense expense = expenseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(ExpenseNotFoundException::new);

        expense.setAmount(expenseUpdateDTO.getAmount());
        expense.setCurrencyCode(expenseUpdateDTO.getCurrencyCode());
        expense.setNote(expenseUpdateDTO.getNote());

        expenseRepository.save(expense);
    }

    public void delete(String id, boolean hardDelete) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(ExpenseNotFoundException::new);

        if (hardDelete) {
            expenseRepository.delete(expense);
        } else {
            expense.setDeletedAt(LocalDateTime.now());
            expenseRepository.save(expense);
        }
    }

    public Page<Expense> findByUsername(String username, Pageable pageable, boolean fetchDeleted) {
        return fetchDeleted ? expenseRepository.findByUser(userService.findByUsername(username), pageable) : expenseRepository.findByUserAndDeletedAtIsNull(userService.findByUsername(username), pageable);
    }

    public Page<Expense> findAll(Pageable pageable, boolean fetchDeleted) {
        return fetchDeleted ? expenseRepository.findAll(pageable) : expenseRepository.findAllByDeletedAtIsNull(pageable);
    }

    public void addTag(String expenseId, String tagName) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(ExpenseNotFoundException::new);
        Optional<Tag> tag = tagService.findByName(tagName);

        if (tag.isEmpty()) {
            tagService.create(tagName);
            expense.addTag(tagService.findByName(tagName)
                    .orElseThrow(TagNotFoundException::new));
        } else {
            expense.addTag(tag.get());
        }

        expenseRepository.save(expense);
    }

    public void removeTag(String expenseId, String tagName) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(ExpenseNotFoundException::new);
        Tag tag = tagService.findByName(tagName)
                .orElseThrow(TagNotFoundException::new);

        expense.removeTag(tag);
        expenseRepository.save(expense);
    }

    public boolean isOwner(String expenseId, Authentication authentication) {
        return findById(expenseId, false).map(expense -> expense.getUser()
                .getUsername()
                .equals(authentication.getName())).orElse(false);
    }
}
