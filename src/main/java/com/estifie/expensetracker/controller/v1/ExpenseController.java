package com.estifie.expensetracker.controller.v1;

import com.estifie.expensetracker.dto.expense.ExpenseAddTagDTO;
import com.estifie.expensetracker.dto.expense.ExpenseCreateDTO;
import com.estifie.expensetracker.dto.expense.ExpenseRemoveTagDTO;
import com.estifie.expensetracker.dto.expense.ExpenseUpdateDTO;
import com.estifie.expensetracker.model.Expense;
import com.estifie.expensetracker.response.expense.ExpensesResponse;
import com.estifie.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/expenses")
@Tag(name = "Expenses", description = "Expense management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Operation(
            summary = "Get expenses by username",
            description = "Retrieves a paginated list of expenses for a specific user. Requires either being the owner of the expenses, " +
                    "having MANAGE_EXPENSES permission, or VIEW_EXPENSES permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved expenses",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true,
                                        "data": {
                                            "expenses": [
                                                {
                                                    "id": "01HQ5G8ZXKCP7RJKF8SBNX4ES",
                                                    "currencyCode": "USD",
                                                    "amount": "50.00",
                                                    "note": "Grocery shopping"
                                                }
                                            ]
                                        }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{username}")
    @PreAuthorize("authentication.principal.username.equals(#username) or hasAuthority('MANAGE_EXPENSES') or hasAuthority('VIEW_EXPENSES')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<ExpensesResponse>> getExpensesByUsername(
            @Parameter(description = "Username of the expense owner") @PathVariable String username,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.<ExpensesResponse>success()
                .data(ExpensesResponse.fromPaginatedExpenses(expenseService.findByUsername(username, PageRequest.of(page, size), false))));
    }

    @Operation(
            summary = "Create a new expense",
            description = "Creates a new expense for a specific user. Requires either being the owner or having MANAGE_EXPENSES permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Expense created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/user/{username}")
    @PreAuthorize("authentication.principal.username.equals(#username) or hasAuthority('MANAGE_EXPENSES')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<Void>> createExpense(
            @Parameter(description = "Username of the expense owner") @PathVariable String username,
            @Parameter(
                    description = "Expense details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseCreateDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "currencyCode": "USD",
                                        "amount": 50.00,
                                        "note": "Grocery shopping"
                                    }
                                    """)
                    )
            )
            @Valid @RequestBody ExpenseCreateDTO expenseCreateDTO
    ) {
        expenseService.create(username, expenseCreateDTO);
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.success());
    }

    @Operation(
            summary = "Delete an expense",
            description = "Soft or hard deletes an expense. Requires either being the owner or having MANAGE_EXPENSES permission. " +
                    "Hard deletion additionally requires HARD_DELETE_EXPENSE permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Expense deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@expenseService.isOwner(#id, authentication) or hasAuthority('MANAGE_EXPENSES')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<Void>> deleteExpense(
            @Parameter(description = "Expense ID") @PathVariable String id,
            @Parameter(description = "Whether to permanently delete the expense") @RequestParam(required = false) boolean hardDelete
    ) {
        expenseService.delete(id, hardDelete);
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.success());
    }

    @Operation(
            summary = "Get expense by ID",
            description = "Retrieves a specific expense by its ID. Requires either being the owner, having MANAGE_EXPENSES permission, " +
                    "or VIEW_EXPENSES permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved expense",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true,
                                        "data": {
                                            "id": "01HQ5G8ZXKCP7RJKF8SBNX4ES",
                                            "amount": 50.00,
                                            "currencyCode": "USD",
                                            "note": "Grocery shopping",
                                            "createdAt": "2024-03-15T10:30:00",
                                            "updatedAt": "2024-03-15T10:30:00"
                                        }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@expenseService.isOwner(#id, authentication) or hasAuthority('MANAGE_EXPENSES') or hasAuthority('VIEW_EXPENSES')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<Optional<Expense>>> getExpense(
            @Parameter(description = "Expense ID") @PathVariable String id
    ) {
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.<Optional<Expense>>success()
                .data(expenseService.findById(id, false)));
    }

    @Operation(
            summary = "Add a tag to an expense",
            description = "Adds a tag to a specific expense. Creates the tag if it doesn't exist. Requires either being the owner " +
                    "or having MANAGE_EXPENSES permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tag added successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    @PostMapping("/{id}/tags")
    @PreAuthorize("@expenseService.isOwner(#id, authentication) or hasAuthority('MANAGE_EXPENSES')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<Void>> addTag(
            @Parameter(description = "Expense ID") @PathVariable String id,
            @Parameter(
                    description = "Tag details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseAddTagDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "tag": "groceries"
                                    }
                                    """)
                    )
            )
            @Valid @RequestBody ExpenseAddTagDTO expenseAddTagDTO
    ) {
        expenseService.addTag(id, expenseAddTagDTO.getTag());
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.success());
    }

    @Operation(
            summary = "Remove a tag from an expense",
            description = "Removes a tag from a specific expense. Requires either being the owner or having MANAGE_EXPENSES permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tag removed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Expense or tag not found")
    })
    @DeleteMapping("/{id}/tags")
    @PreAuthorize("@expenseService.isOwner(#id, authentication) or hasAuthority('MANAGE_EXPENSES')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<Void>> removeTag(
            @Parameter(description = "Expense ID") @PathVariable String id,
            @Parameter(
                    description = "Tag details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseRemoveTagDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "tag": "groceries"
                                    }
                                    """)
                    )
            )
            @Valid @RequestBody ExpenseRemoveTagDTO expenseRemoveTagDTO
    ) {
        expenseService.removeTag(id, expenseRemoveTagDTO.getTag());
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.success());
    }

    @Operation(
            summary = "Get all expenses",
            description = "Retrieves a paginated list of all expenses. Requires either MANAGE_EXPENSES or VIEW_EXPENSES permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved expenses",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true,
                                        "data": {
                                            "expenses": [
                                                {
                                                    "id": "01HQ5G8ZXKCP7RJKF8SBNX4ES",
                                                    "currencyCode": "USD",
                                                    "amount": "50.00",
                                                    "note": "Grocery shopping"
                                                }
                                            ]
                                        }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @GetMapping("/")
    @PreAuthorize("hasAuthority('MANAGE_EXPENSES') or hasAuthority('VIEW_EXPENSES')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<ExpensesResponse>> getExpenses(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.<ExpensesResponse>success()
                .data(ExpensesResponse.fromPaginatedExpenses(expenseService.findAll(PageRequest.of(page, size), false))));
    }

    @Operation(
            summary = "Update an expense",
            description = "Updates an existing expense. Requires either being the owner or having MANAGE_EXPENSES permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Expense updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("@expenseService.isOwner(#id, authentication) or hasAuthority('MANAGE_EXPENSES')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<Void>> updateExpense(
            @Parameter(description = "Expense ID") @PathVariable String id,
            @Parameter(
                    description = "Updated expense details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseUpdateDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "currencyCode": "USD",
                                        "amount": 75.00,
                                        "note": "Updated grocery shopping"
                                    }
                                    """)
                    )
            )
            @Valid @RequestBody ExpenseUpdateDTO expenseUpdateDTO
    ) {
        expenseService.update(id, expenseUpdateDTO);
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.success());
    }
}
