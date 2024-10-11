package com.estifie.expensetracker.exception.subscription;

import com.estifie.expensetracker.exception.global.NotFoundException;

public class SubscriptionNotFoundException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "Subscription not found";

    public SubscriptionNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}
