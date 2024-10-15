package com.estifie.expensetracker.dto.subscription;

import com.estifie.expensetracker.enums.SubscriptionType;
import com.estifie.expensetracker.model.Subscription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SubscriptionCreateDTO {
    @NotBlank
    private final String name;

    @NotBlank
    private final LocalDate startDate;

    @NotBlank
    private final SubscriptionType type;

    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    private String currencyCode;

    public SubscriptionCreateDTO(String name, BigDecimal amount, LocalDate startDate, SubscriptionType type, String currencyCode) {
        this.name = name;
        this.amount = amount;
        this.startDate = startDate;
        this.type = type;
        this.currencyCode = currencyCode;
    }

    public @NotBlank String getName() {
        return name;
    }

    public @NotBlank BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(@NotBlank BigDecimal amount) {
        this.amount = amount;
    }

    public @NotBlank LocalDate getStartDate() {
        return startDate;
    }

    public @NotBlank SubscriptionType getType() {
        return type;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Subscription toSubscription() {
        return new Subscription(name, amount, startDate, type, currencyCode);
    }
}
