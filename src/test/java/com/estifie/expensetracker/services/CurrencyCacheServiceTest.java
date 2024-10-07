package com.estifie.expensetracker.services;

import com.estifie.expensetracker.service.CurrencyCacheServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencyCacheServiceTest {
    private CurrencyCacheServiceImpl currencyCacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyCacheService = new CurrencyCacheServiceImpl(2, TimeUnit.SECONDS);
    }


    @Test
    void testGet_PreviouslyCached() {
        String pair = "USDTRY";
        BigDecimal rate = BigDecimal.valueOf(35.25);

        currencyCacheService.put(pair, rate);

        assertFalse(currencyCacheService.get(pair)
                .isEmpty());
        assertEquals(rate, currencyCacheService.get(pair)
                .get());
    }

    @Test
    void testGet_NotCached() {
        String pair = "USDTRY";

        assertTrue(currencyCacheService.get(pair)
                .isEmpty());
    }

    @Test
    void testGet_AfterExpiry() throws InterruptedException {
        String pair = "USDTRY";
        BigDecimal rate = BigDecimal.valueOf(35.25);

        currencyCacheService.put(pair, rate);

        TimeUnit.SECONDS.sleep(3);

        assertTrue(currencyCacheService.get(pair)
                .isEmpty());
    }

    @Test
    void testPut() {
        String pair = "USDTRY";
        BigDecimal rate = BigDecimal.valueOf(35.25);

        currencyCacheService.put(pair, rate);

        assertEquals(rate, currencyCacheService.get(pair)
                .get());
    }

    @Test
    void testInvalidate() {
        String pair = "USDTRY";
        BigDecimal rate = BigDecimal.valueOf(35.25);

        currencyCacheService.put(pair, rate);

        assertTrue(currencyCacheService.get(pair)
                .isPresent());

        currencyCacheService.invalidate(pair);

        assertTrue(currencyCacheService.get(pair)
                .isEmpty());
    }
}
