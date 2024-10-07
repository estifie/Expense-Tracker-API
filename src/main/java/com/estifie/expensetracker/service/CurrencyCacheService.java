package com.estifie.expensetracker.service;

import java.math.BigDecimal;
import java.util.Optional;

public interface CurrencyCacheService {
    void put(String pair, BigDecimal rate);

    Optional<BigDecimal> get(String pair);

    void invalidate(String pair);
}
