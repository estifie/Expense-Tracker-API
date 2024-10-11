package com.estifie.expensetracker.response.subscription;

import com.estifie.expensetracker.enums.SubscriptionType;
import com.estifie.expensetracker.model.Subscription;

import java.math.BigDecimal;

public record SubscriptionResponse(String id, String name, BigDecimal amount,
                                   String currencyCode, SubscriptionType type,
                                   String startDate, String nextBillingDate,
                                   boolean active) {

    public static SubscriptionResponse fromSubscription(Subscription subscription) {
        return new SubscriptionResponse(subscription.getId(), subscription.getName(), subscription.getAmount(), subscription.getCurrencyCode(), subscription.getType(), subscription.getStartDate()
                .toString(), subscription.getNextBillingDate()
                .toString(), subscription.isActive());
    }


}
