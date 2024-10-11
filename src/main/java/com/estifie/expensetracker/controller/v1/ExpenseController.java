package com.estifie.expensetracker.controller.v1;

import com.estifie.expensetracker.annotations.RequiresAnyPermission;
import com.estifie.expensetracker.dto.expense.ExpenseCreateDTO;
import com.estifie.expensetracker.enums.Permission;
import com.estifie.expensetracker.model.Expense;
import com.estifie.expensetracker.response.ApiResponse;
import com.estifie.expensetracker.response.expense.ExpensesResponse;
import com.estifie.expensetracker.service.ExpenseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/user/{username}")
    @RequiresAnyPermission({Permission.OWNERSHIP, Permission.MANAGE_EXPENSES, Permission.VIEW_EXPENSES})
    public ResponseEntity<ApiResponse<ExpensesResponse>> getExpensesByUsername(@PathVariable String username, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(ApiResponse.<ExpensesResponse>success()
                .data(ExpensesResponse.fromPaginatedExpenses(expenseService.findByUsername(username, PageRequest.of(page, size), false))));
    }

    @PostMapping("/user/{username}")
    @RequiresAnyPermission({Permission.OWNERSHIP, Permission.MANAGE_EXPENSES})
    public ResponseEntity<ApiResponse<Void>> createExpense(@PathVariable String username, @RequestBody ExpenseCreateDTO expenseCreateDTO) {
        expenseService.create(username, expenseCreateDTO);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{id}")
    @RequiresAnyPermission({Permission.OWNERSHIP, Permission.MANAGE_EXPENSES})
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable String id, @RequestParam(required = false) boolean hardDelete) {
        expenseService.delete(id, hardDelete);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/{id}")
    @RequiresAnyPermission({Permission.OWNERSHIP, Permission.MANAGE_EXPENSES, Permission.VIEW_EXPENSES})
    public ResponseEntity<ApiResponse<Optional<Expense>>> getExpense(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<Optional<Expense>>success()
                .data(expenseService.findById(id, false)));
    }

    @GetMapping("/")
    @RequiresAnyPermission({Permission.MANAGE_EXPENSES, Permission.VIEW_EXPENSES})
    public ResponseEntity<ApiResponse<ExpensesResponse>> getExpenses(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(ApiResponse.<ExpensesResponse>success()
                .data(ExpensesResponse.fromPaginatedExpenses(expenseService.findAll(PageRequest.of(page, size), false))));
    }
}
