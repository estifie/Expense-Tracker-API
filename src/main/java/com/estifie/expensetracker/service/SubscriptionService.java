package com.estifie.expensetracker.service;

import com.estifie.expensetracker.dto.subscription.SubscriptionCreateDTO;
import com.estifie.expensetracker.model.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SubscriptionService {
    void create(String username, SubscriptionCreateDTO subscriptionCreateDTO);

    void activate(String id);

    void deactivate(String id);

    void delete(String id, boolean hardDelete);

    Optional<Subscription> findById(String id, boolean fetchDeleted);

    Page<Subscription> findByUsername(String username, Pageable pageable, boolean fetchDeleted);

    Page<Subscription> findAll(Pageable pageable, boolean fetchDeleted);

}
