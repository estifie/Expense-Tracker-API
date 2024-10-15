package com.estifie.expensetracker.service;

import com.estifie.expensetracker.dto.expense.ExpenseCreateDTO;
import com.estifie.expensetracker.dto.subscription.SubscriptionCreateDTO;
import com.estifie.expensetracker.enums.Permission;
import com.estifie.expensetracker.exception.auth.AuthorizationException;
import com.estifie.expensetracker.exception.subscription.SubscriptionNotFoundException;
import com.estifie.expensetracker.exception.user.UserNotFoundException;
import com.estifie.expensetracker.model.Subscription;
import com.estifie.expensetracker.model.User;
import com.estifie.expensetracker.repository.SubscriptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Component("subscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final ExpenseService expenseService;
    private final UserService userService;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, ExpenseService expenseService, UserService userService) {
        this.subscriptionRepository = subscriptionRepository;
        this.expenseService = expenseService;
        this.userService = userService;
    }

    public void create(String username, SubscriptionCreateDTO subscriptionCreateDTO) {
        User user = userService.findByUsername(username);
        if (user == null) throw new UserNotFoundException();

        Subscription subscription = subscriptionCreateDTO.toSubscription();
        subscription.setUser(user);

        subscriptionRepository.save(subscription);
    }

    public void activate(String id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(SubscriptionNotFoundException::new);

        subscription.setActive(true);

        subscriptionRepository.save(subscription);
    }

    public void deactivate(String id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(SubscriptionNotFoundException::new);

        subscription.setActive(false);

        subscriptionRepository.save(subscription);
    }

    public void delete(String id, boolean hardDelete) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(SubscriptionNotFoundException::new);
        subscription.setDeletedAt(LocalDateTime.now());

        if (!hardDelete) {
            subscriptionRepository.save(subscription);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        boolean canHardDelete =
                userService.hasPermission(authentication.getName(), Permission.HARD_DELETE_SUBSCRIPTION.name());

        if (!canHardDelete) {
            throw new AuthorizationException("You do not have permission to perform this action");
        }

        subscriptionRepository.deleteById(id);
    }

    public Optional<Subscription> findById(String id, boolean fetchDeleted) {
        return fetchDeleted ? subscriptionRepository.findById(id) : subscriptionRepository.findByIdAndDeletedAtIsNull(id);
    }

    public Page<Subscription> findByUsername(String username, Pageable pageable, boolean fetchDeleted) {
        return fetchDeleted ? subscriptionRepository.findByUser(userService.findByUsername(username), pageable) : subscriptionRepository.findByUserAndDeletedAtIsNull(userService.findByUsername(username), pageable);
    }

    public Page<Subscription> findAll(Pageable pageable, boolean fetchDeleted) {
        return fetchDeleted ? subscriptionRepository.findAll(pageable) : subscriptionRepository.findAllByDeletedAtIsNull(pageable);
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void processRecurringPayments() {
        List<Subscription> subscriptions = subscriptionRepository.findAllByNextBillingDate(LocalDate.now());

        for (Subscription subscription : subscriptions) {
            processSubscription(subscription);
        }
    }

    private void processSubscription(Subscription subscription) {
        ExpenseCreateDTO expenseCreateDTO = new ExpenseCreateDTO(subscription.getCurrencyCode(), subscription.getAmount(), "Subscription: " + subscription.getName());

        expenseService.create(subscription.getUser()
                .getUsername(), expenseCreateDTO);

        subscription.updateNextBillingDate();
        subscriptionRepository.save(subscription);
    }

    public boolean isOwner(String subscriptionId, String username) {
        return findById(subscriptionId, false).map(subscription -> subscription.getUser()
                .getUsername()
                .equals(username)).orElse(false);
    }

}
