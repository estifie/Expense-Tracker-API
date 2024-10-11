package com.estifie.expensetracker.controller.v1;

import com.estifie.expensetracker.annotations.RequiresAnyPermission;
import com.estifie.expensetracker.dto.subscription.SubscriptionCreateDTO;
import com.estifie.expensetracker.enums.Permission;
import com.estifie.expensetracker.exception.subscription.SubscriptionNotFoundException;
import com.estifie.expensetracker.response.ApiResponse;
import com.estifie.expensetracker.response.subscription.SubscriptionResponse;
import com.estifie.expensetracker.response.subscription.SubscriptionsResponse;
import com.estifie.expensetracker.service.SubscriptionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/user/{username}")
    @RequiresAnyPermission({Permission.OWNERSHIP, Permission.MANAGE_SUBSCRIPTIONS, Permission.VIEW_SUBSCRIPTIONS})
    public ResponseEntity<ApiResponse<SubscriptionsResponse>> getSubscriptionsByUsername(@PathVariable String username, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(ApiResponse.<SubscriptionsResponse>success()
                .data(SubscriptionsResponse.fromPaginatedSubscriptions(subscriptionService.findByUsername(username, PageRequest.of(page, size), false))));
    }

    @PostMapping("/user/{username}")
    @RequiresAnyPermission({Permission.OWNERSHIP, Permission.MANAGE_SUBSCRIPTIONS})
    public ResponseEntity<ApiResponse<Void>> createSubscription(@PathVariable String username, @RequestBody SubscriptionCreateDTO subscriptionCreateDTO) {
        subscriptionService.create(username, subscriptionCreateDTO);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{id}")
    @RequiresAnyPermission({Permission.OWNERSHIP, Permission.MANAGE_SUBSCRIPTIONS})
    public ResponseEntity<ApiResponse<Void>> deleteSubscription(@PathVariable String id, @RequestParam(required = false) boolean hardDelete) {
        subscriptionService.delete(id, hardDelete);

        return ResponseEntity.ok(ApiResponse.success());
    }

    @PutMapping("/{id}/deactivate")
    @RequiresAnyPermission({Permission.OWNERSHIP, Permission.MANAGE_SUBSCRIPTIONS})
    public ResponseEntity<ApiResponse<Void>> deactivateSubscription(@PathVariable String id) {
        subscriptionService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/{id}")
    @RequiresAnyPermission({Permission.OWNERSHIP, Permission.MANAGE_SUBSCRIPTIONS, Permission.VIEW_SUBSCRIPTIONS})
    public ResponseEntity<ApiResponse<SubscriptionResponse>> getSubscription(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<SubscriptionResponse>success()
                .data(SubscriptionResponse.fromSubscription(subscriptionService.findById(id, false)
                        .orElseThrow(SubscriptionNotFoundException::new))));
    }

    @GetMapping("/")
    @RequiresAnyPermission({Permission.MANAGE_SUBSCRIPTIONS, Permission.VIEW_SUBSCRIPTIONS})
    public ResponseEntity<ApiResponse<SubscriptionsResponse>> getSubscriptions(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(ApiResponse.<SubscriptionsResponse>success()
                .data(SubscriptionsResponse.fromPaginatedSubscriptions(subscriptionService.findAll(PageRequest.of(page, size), false))));
    }


}
