package com.estifie.expensetracker.service;

import com.estifie.expensetracker.exception.currency.CurrencyConversionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {
    private final CurrencyCacheService currencyCacheService;
    private final RestTemplate restTemplate;
    private final String currencyConversionApiUrl;
    private final String currencyConversionApiKey;

    public CurrencyConversionServiceImpl(CurrencyCacheService currencyCacheService, RestTemplate restTemplate, @Value("${currency-conversion.api.url}") String currencyConversionApiUrl, @Value("${currency-conversion.api.key}") String currencyConversionApiKey) {
        this.currencyCacheService = currencyCacheService;
        this.restTemplate = restTemplate;
        this.currencyConversionApiUrl = currencyConversionApiUrl;
        this.currencyConversionApiKey = currencyConversionApiKey;
    }

    public BigDecimal convertCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        String currencyPair = fromCurrency + toCurrency;

        BigDecimal exchangeRate = currencyCacheService.get(currencyPair)
                .orElseGet(() -> getExchangeRateExternal(fromCurrency, toCurrency));

        return amount.multiply(exchangeRate);
    }

    private BigDecimal getExchangeRateExternal(String fromCurrency, String toCurrency) {
        String currencyPair = fromCurrency + toCurrency;

        try {


            String url = UriComponentsBuilder.fromHttpUrl(currencyConversionApiUrl)
                    .queryParam("apiKey", currencyConversionApiKey)
                    .queryParam("currencies", toCurrency)
                    .queryParam("base", fromCurrency)
                    .toUriString();

            System.out.println("Constructed URL: " + url);

            ParameterizedTypeReference<Map<String, Map<String, BigDecimal>>> responseType = new ParameterizedTypeReference<>() {
            };

            Map<String, Map<String, BigDecimal>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType)
                    .getBody();

            if (response == null || response.get("data") == null) {
                throw new CurrencyConversionException("No exchange rate available for " + currencyPair);
            }

            BigDecimal exchangeRate = response.get("data")
                    .get(toCurrency);
            if (exchangeRate == null) {
                throw new CurrencyConversionException("No exchange rate available for " + currencyPair);
            }

            currencyCacheService.put(currencyPair, exchangeRate);

            return exchangeRate;
        } catch (Exception e) {
            throw new CurrencyConversionException("Error while fetching exchange rate");
        }
    }
}
