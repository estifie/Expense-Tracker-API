package com.estifie.expensetracker.services;

import com.estifie.expensetracker.exception.currency.CurrencyConversionException;
import com.estifie.expensetracker.service.CurrencyCacheService;
import com.estifie.expensetracker.service.CurrencyConversionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CurrencyConversionServiceTest {
    private CurrencyConversionServiceImpl currencyConversionService;

    @Mock
    private CurrencyCacheService currencyCacheService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyConversionService = new CurrencyConversionServiceImpl(currencyCacheService, restTemplate, "https://api.url.com", "apiKey");
    }

    @Test
    void testConvertCurrency_FromCache() {
        String from = "USD";
        String to = "TRY";
        BigDecimal rate = BigDecimal.valueOf(35.25);
        BigDecimal amount = BigDecimal.valueOf(100);

        currencyCacheService.put(from + to, rate);

        assertEquals(rate.multiply(amount), currencyConversionService.convertCurrency(from, to, amount));
    }

    @Test
    void testConvertCurrency_FromExternal() {
        String fromCurrency = "USD";
        String toCurrency = "TRY";

        BigDecimal exchangeRate = new BigDecimal("34.2435547828");
        Map<String, Map<String, BigDecimal>> apiResponse = new HashMap<>();
        Map<String, BigDecimal> data = new HashMap<>();
        data.put(toCurrency, exchangeRate);
        apiResponse.put("data", data);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(apiResponse));

        BigDecimal result = currencyConversionService.convertCurrency(fromCurrency, toCurrency, BigDecimal.ONE);

        assertEquals(exchangeRate, result);

        verify(currencyCacheService).put(fromCurrency + toCurrency, exchangeRate);
    }

    @Test
    void testConvertCurrency_NoRate() {
        String fromCurrency = "USD";
        String toCurrency = "TRY";

        Map<String, Map<String, BigDecimal>> apiResponse = new HashMap<>();
        apiResponse.put("data", new HashMap<>());

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(apiResponse));

        assertThrows(CurrencyConversionException.class, () -> currencyConversionService.convertCurrency(fromCurrency, toCurrency, BigDecimal.ONE));

        verify(currencyCacheService, never()).put(anyString(), any(BigDecimal.class));
    }
}
