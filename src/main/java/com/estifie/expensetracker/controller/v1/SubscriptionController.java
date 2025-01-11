package com.estifie.expensetracker.controller.v1;

import com.estifie.expensetracker.dto.subscription.SubscriptionCreateDTO;
import com.estifie.expensetracker.exception.subscription.SubscriptionNotFoundException;
import com.estifie.expensetracker.response.subscription.SubscriptionResponse;
import com.estifie.expensetracker.response.subscription.SubscriptionsResponse;
import com.estifie.expensetracker.service.SubscriptionService;
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

@RestController
@RequestMapping("/v1/subscriptions")
@Tag(name = "Subscriptions", description = "Subscription management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Operation(
            summary = "Get subscriptions by username",
            description = "Retrieves a paginated list of subscriptions for a specific user. Requires either being the owner, " +
                    "having MANAGE_SUBSCRIPTIONS permission, or VIEW_SUBSCRIPTIONS permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved subscriptions",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionsResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true,
                                        "data": {
                                            "subscriptions": [
                                                {
                                                    "id": "01HQ5G8ZXKCP7RJKF8SBNX4ES",
                                                    "name": "Netflix",
                                                    "amount": 14.99,
                                                    "currencyCode": "USD",
                                                    "type": "MONTHLY",
                                                    "startDate": "2024-03-15",
                                                    "nextBillingDate": "2024-04-15",
                                                    "active": true
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
    @PreAuthorize("authentication.principal.username.equals(#username) or hasAuthority('MANAGE_SUBSCRIPTIONS') or hasAuthority('VIEW_SUBSCRIPTIONS')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<SubscriptionsResponse>> getSubscriptionsByUsername(
            @Parameter(description = "Username of the subscription owner") @PathVariable String username,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.<SubscriptionsResponse>success()
                .data(SubscriptionsResponse.fromPaginatedSubscriptions(subscriptionService.findByUsername(username, PageRequest.of(page, size), false))));
    }

    @Operation(
            summary = "Create a new subscription",
            description = "Creates a new subscription for a specific user. Requires either being the owner or having MANAGE_SUBSCRIPTIONS permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subscription created successfully",
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
    @PreAuthorize("authentication.principal.username.equals(#username) or hasAuthority('MANAGE_SUBSCRIPTIONS')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<Void>> createSubscription(
            @Parameter(description = "Username of the subscription owner") @PathVariable String username,
            @Parameter(
                    description = "Subscription details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionCreateDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "name": "Netflix",
                                        "amount": 14.99,
                                        "currencyCode": "USD",
                                        "startDate": "2024-03-15",
                                        "type": "MONTHLY"
                                    }
                                    """)
                    )
            )
            @Valid @RequestBody SubscriptionCreateDTO subscriptionCreateDTO
    ) {
        subscriptionService.create(username, subscriptionCreateDTO);
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.success());
    }

    @Operation(
            summary = "Delete a subscription",
            description = "Soft or hard deletes a subscription. Requires either being the owner or having MANAGE_SUBSCRIPTIONS permission. " +
                    "Hard deletion additionally requires HARD_DELETE_SUBSCRIPTION permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subscription deleted successfully",
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
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@subscriptionService.isOwner(#id, authentication) or hasAuthority('MANAGE_SUBSCRIPTIONS')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<Void>> deleteSubscription(
            @Parameter(description = "Subscription ID") @PathVariable String id,
            @Parameter(description = "Whether to permanently delete the subscription") @RequestParam(required = false) boolean hardDelete
    ) {
        subscriptionService.delete(id, hardDelete);
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.success());
    }

    @Operation(
            summary = "Deactivate a subscription",
            description = "Deactivates a subscription, preventing it from generating new expenses. " +
                    "Requires either being the owner or having MANAGE_SUBSCRIPTIONS permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subscription deactivated successfully",
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
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("@subscriptionService.isOwner(#id, authentication) or hasAuthority('MANAGE_SUBSCRIPTIONS')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<Void>> deactivateSubscription(
            @Parameter(description = "Subscription ID") @PathVariable String id
    ) {
        subscriptionService.deactivate(id);
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.success());
    }

    @Operation(
            summary = "Activate a subscription",
            description = "Activates a subscription, allowing it to generate new expenses. " +
                    "Requires either being the owner or having MANAGE_SUBSCRIPTIONS permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Subscription activated successfully",
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
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    @PutMapping("/{id}/activate")
    @PreAuthorize("@subscriptionService.isOwner(#id, authentication) or hasAuthority('MANAGE_SUBSCRIPTIONS')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<Void>> activateSubscription(
            @Parameter(description = "Subscription ID") @PathVariable String id
    ) {
        subscriptionService.activate(id);
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.success());
    }

    @Operation(
            summary = "Get subscription by ID",
            description = "Retrieves a specific subscription by its ID. Requires either being the owner, having MANAGE_SUBSCRIPTIONS permission, " +
                    "or VIEW_SUBSCRIPTIONS permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved subscription",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true,
                                        "data": {
                                            "id": "01HQ5G8ZXKCP7RJKF8SBNX4ES",
                                            "name": "Netflix",
                                            "amount": 14.99,
                                            "currencyCode": "USD",
                                            "type": "MONTHLY",
                                            "startDate": "2024-03-15",
                                            "nextBillingDate": "2024-04-15",
                                            "active": true
                                        }
                                    }
                                    """)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@subscriptionService.isOwner(#id, authentication) or hasAuthority('MANAGE_SUBSCRIPTIONS') or hasAuthority('VIEW_SUBSCRIPTIONS')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<SubscriptionResponse>> getSubscription(
            @Parameter(description = "Subscription ID") @PathVariable String id
    ) {
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.<SubscriptionResponse>success()
                .data(SubscriptionResponse.fromSubscription(subscriptionService.findById(id, false)
                        .orElseThrow(SubscriptionNotFoundException::new))));
    }

    @Operation(
            summary = "Get all subscriptions",
            description = "Retrieves a paginated list of all subscriptions. Requires either MANAGE_SUBSCRIPTIONS or VIEW_SUBSCRIPTIONS permission."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved subscriptions",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionsResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "success": true,
                                        "data": {
                                            "subscriptions": [
                                                {
                                                    "id": "01HQ5G8ZXKCP7RJKF8SBNX4ES",
                                                    "name": "Netflix",
                                                    "amount": 14.99,
                                                    "currencyCode": "USD",
                                                    "type": "MONTHLY",
                                                    "startDate": "2024-03-15",
                                                    "nextBillingDate": "2024-04-15",
                                                    "active": true
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
    @PreAuthorize("hasAuthority('MANAGE_SUBSCRIPTIONS') or hasAuthority('VIEW_SUBSCRIPTIONS')")
    public ResponseEntity<com.estifie.expensetracker.response.ApiResponse<SubscriptionsResponse>> getSubscriptions(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(com.estifie.expensetracker.response.ApiResponse.<SubscriptionsResponse>success()
                .data(SubscriptionsResponse.fromPaginatedSubscriptions(subscriptionService.findAll(PageRequest.of(page, size), false))));
    }
}
