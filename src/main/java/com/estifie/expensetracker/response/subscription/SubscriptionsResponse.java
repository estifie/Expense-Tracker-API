package com.estifie.expensetracker.response.subscription;

import com.estifie.expensetracker.model.Subscription;
import org.springframework.data.domain.Page;

import java.util.List;

public record SubscriptionsResponse(List<SubscriptionResponse> subscriptions) {

    public static SubscriptionsResponse fromSubscriptions(List<SubscriptionResponse> subscriptions) {
        return new SubscriptionsResponse(subscriptions);
    }

    public static SubscriptionsResponse fromPaginatedSubscriptions(Page<Subscription> subscriptions) {
        List<Subscription> subscriptionList = subscriptions.getContent();

        return new SubscriptionsResponse(subscriptionList.stream()
                .map(SubscriptionResponse::fromSubscription)
                .toList());
    }

    public List<SubscriptionResponse> getSubscriptions() {
        return subscriptions;
    }
}
