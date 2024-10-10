package com.estifie.expensetracker.repository;

import com.estifie.expensetracker.model.Subscription;
import com.estifie.expensetracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
    List<Subscription> findAllByNextPaymentDate(LocalDate paymentDate);

    Page<Subscription> findByUser(User user, Pageable pageable);

    Page<Subscription> findByUserAndDeletedAtIsNull(User user, Pageable pageable);

    Optional<Subscription> findByIdAndDeletedAtIsNull(String id);

    Page<Subscription> findAllByDeletedAtIsNull(Pageable pageable);
}
