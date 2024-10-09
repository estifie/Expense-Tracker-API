package com.estifie.expensetracker.controller.v1;

import com.estifie.expensetracker.annotations.RequiresAnyPermission;
import com.estifie.expensetracker.dto.expense.ExpenseCreateDTO;
import com.estifie.expensetracker.enums.Permission;
import com.estifie.expensetracker.model.Expense;
import com.estifie.expensetracker.response.ApiResponse;
import com.estifie.expensetracker.service.ExpenseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/user/{username}")
    @RequiresAnyPermission({Permission.OWNERSHIP, Permission.MANAGE_EXPENSES, Permission.VIEW_EXPENSES})
    public ResponseEntity<ApiResponse<Page<Expense>>> getExpensesByUsername(@PathVariable String username, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(ApiResponse.<Page<Expense>>success()
                .data(expenseService.findByUsername(username, PageRequest.of(page, size))));
    }

    @PostMapping("/user/{username}")
    @RequiresAnyPermission({Permission.OWNERSHIP, Permission.MANAGE_EXPENSES})
    public ResponseEntity<ApiResponse<Void>> createExpense(@PathVariable String username, @RequestBody ExpenseCreateDTO expenseCreateDTO) {
        expenseService.create(username, expenseCreateDTO);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
