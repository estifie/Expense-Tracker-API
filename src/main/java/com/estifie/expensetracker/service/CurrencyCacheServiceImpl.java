package com.estifie.expensetracker.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class CurrencyCacheServiceImpl implements CurrencyCacheService {
    private final Cache<String, BigDecimal> cache;

    public CurrencyCacheServiceImpl() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
                .build();
    }

    public CurrencyCacheServiceImpl(@NonNegative long duration, @NotNull TimeUnit unit) {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(duration, unit)
                .maximumSize(1000)
                .build();
    }

    public void put(String pair, BigDecimal rate) {
        cache.put(pair, rate);
    }

    public Optional<BigDecimal> get(String pair) {
        return Optional.ofNullable(cache.getIfPresent(pair));
    }

    public void invalidate(String pair) {
        cache.invalidate(pair);
    }
}
