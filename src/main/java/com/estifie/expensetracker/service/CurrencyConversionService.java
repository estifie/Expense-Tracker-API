package com.estifie.expensetracker.service;

import java.math.BigDecimal;

public interface CurrencyConversionService {
    BigDecimal convertCurrency(String fromCurrency, String toCurrency, BigDecimal amount);
}
