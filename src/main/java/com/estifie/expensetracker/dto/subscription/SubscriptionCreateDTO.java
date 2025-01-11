package com.estifie.expensetracker.dto.subscription;

import com.estifie.expensetracker.enums.SubscriptionType;
import com.estifie.expensetracker.model.Subscription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Data transfer object for creating a new subscription")
public class SubscriptionCreateDTO {
    @NotBlank
    @Schema(
            description = "Name of the subscription",
            example = "Netflix",
            minLength = 1,
            maxLength = 255,
            required = true
    )
    private final String name;

    @NotNull
    @Schema(
            description = "Start date of the subscription (YYYY-MM-DD)",
            example = "2024-03-15",
            required = true,
            pattern = "^\\d{4}-\\d{2}-\\d{2}$"
    )
    private final LocalDate startDate;

    @NotNull
    @Schema(
            description = "Type of subscription billing cycle",
            example = "MONTHLY",
            allowableValues = {"DAILY", "WEEKLY", "MONTHLY", "YEARLY"},
            required = true
    )
    private final SubscriptionType type;

    @NotNull
    @PositiveOrZero
    @Schema(
            description = "Amount to be charged for each billing cycle",
            example = "14.99",
            minimum = "0",
            required = true
    )
    private BigDecimal amount;

    @Schema(
            description = "Currency code for the subscription amount (e.g., USD, EUR)",
            example = "USD",
            defaultValue = "USD",
            pattern = "^[A-Z]{3}$"
    )
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public SubscriptionType getType() {
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
