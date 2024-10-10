package com.estifie.expensetracker.model;

import com.estifie.expensetracker.enums.SubscriptionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "subscriptions")
@Entity
public class Subscription {
    @Id
    private String id;

    @Column
    private String name;

    @Column
    private BigDecimal amount;

    @Column
    private String currencyCode;

    @Enumerated(EnumType.STRING)
    private SubscriptionType type;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate nextBillingDate;

    @Column
    private boolean active;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    public Subscription() {
        this.id = new ULID().nextULID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deletedAt = null;
    }

    public Subscription(String name, BigDecimal amount, LocalDate startDate, SubscriptionType type, String currencyCode) {
        this();
        this.name = name;
        this.amount = amount;
        this.startDate = startDate;
        this.type = type;
        this.currencyCode = currencyCode;
        this.nextBillingDate = startDate;
        this.active = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public SubscriptionType getType() {
        return type;
    }

    public void setType(SubscriptionType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getNextBillingDate() {
        return nextBillingDate;
    }

    public void setNextBillingDate(LocalDate nextBillingDate) {
        this.nextBillingDate = nextBillingDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Expense generateExpense() {
        return new Expense(amount, currencyCode, name);
    }

    public void updateNextBillingDate() {
        switch (type) {
            case DAILY:
                nextBillingDate = nextBillingDate.plusDays(1);
                break;
            case WEEKLY:
                nextBillingDate = nextBillingDate.plusWeeks(1);
                break;
            case MONTHLY:
                nextBillingDate = nextBillingDate.plusMonths(1);
                break;
            case YEARLY:
                nextBillingDate = nextBillingDate.plusYears(1);
                break;
        }
    }
}
