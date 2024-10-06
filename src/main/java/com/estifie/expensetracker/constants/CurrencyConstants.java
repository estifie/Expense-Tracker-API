package com.estifie.expensetracker.constants;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CurrencyConstants {

    private final Set<String> supportedCurrencies = new HashSet<>();

    @PostConstruct
    public void init() {
        supportedCurrencies.addAll(Currency.getAvailableCurrencies()
                .stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toSet()));
    }

    public Set<String> getSupportedCurrencies() {
        return supportedCurrencies;
    }

    public boolean isSupportedCurrency(String currency) {
        return supportedCurrencies.contains(currency);
    }
}
