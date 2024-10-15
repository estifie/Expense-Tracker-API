package com.estifie.expensetracker.controller.v1;

import com.estifie.expensetracker.dto.expense.ExpenseAddTagDTO;
import com.estifie.expensetracker.dto.expense.ExpenseCreateDTO;
import com.estifie.expensetracker.dto.expense.ExpenseRemoveTagDTO;
import com.estifie.expensetracker.model.Expense;
import com.estifie.expensetracker.response.ApiResponse;
import com.estifie.expensetracker.response.expense.ExpensesResponse;
import com.estifie.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("authentication.principal.username.equals(#username) or hasAuthority('MANAGE_EXPENSES') or hasAuthority('VIEW_EXPENSES')")
    public ResponseEntity<ApiResponse<ExpensesResponse>> getExpensesByUsername(@PathVariable String username, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.<ExpensesResponse>success()
                .data(ExpensesResponse.fromPaginatedExpenses(expenseService.findByUsername(username, PageRequest.of(page, size), false))));
    }

    @PostMapping("/user/{username}")
    @PreAuthorize("authentication.principal.username.equals(#username) or hasAuthority('MANAGE_EXPENSES')")
    public ResponseEntity<ApiResponse<Void>> createExpense(@PathVariable String username, @Valid @RequestBody ExpenseCreateDTO expenseCreateDTO) {
        expenseService.create(username, expenseCreateDTO);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@expenseService.isOwner(#id, authentication) or hasAuthority('MANAGE_EXPENSES')")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable String id, @RequestParam(required = false) boolean hardDelete) {
        expenseService.delete(id, hardDelete);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@expenseService.isOwner(#id, authentication) or " + "hasAuthority('MANAGE_EXPENSES') or hasAuthority('VIEW_EXPENSES')")
    public ResponseEntity<ApiResponse<Optional<Expense>>> getExpense(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<Optional<Expense>>success()
                .data(expenseService.findById(id, false)));
    }

    @PostMapping("/{id}/tags")
    @PreAuthorize("@expenseService.isOwner(#id, authentication) or hasAuthority('MANAGE_EXPENSES')")
    public ResponseEntity<ApiResponse<Void>> addTag(@PathVariable String id, @Valid @RequestBody ExpenseAddTagDTO expenseAddTagDTO) {
        expenseService.addTag(id, expenseAddTagDTO.getTag());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{id}/tags")
    @PreAuthorize("@expenseService.isOwner(#id, authentication) or hasAuthority('MANAGE_EXPENSES')")
    public ResponseEntity<ApiResponse<Void>> removeTag(@PathVariable String id, @Valid @RequestBody ExpenseRemoveTagDTO expenseRemoveTagDTO) {
        expenseService.removeTag(id, expenseRemoveTagDTO.getTag());
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('MANAGE_EXPENSES') or hasAuthority('VIEW_EXPENSES')")
    public ResponseEntity<ApiResponse<ExpensesResponse>> getExpenses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.<ExpensesResponse>success()
                .data(ExpensesResponse.fromPaginatedExpenses(expenseService.findAll(PageRequest.of(page, size), false))));
    }
}
