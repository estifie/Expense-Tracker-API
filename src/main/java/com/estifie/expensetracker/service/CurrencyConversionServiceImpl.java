package com.estifie.expensetracker.service;

import com.estifie.expensetracker.exception.currency.CurrencyConversionException;
import org.springframework.stereotype.Service;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.fx.FxQuote;

import java.math.BigDecimal;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {
    private final CurrencyCacheService currencyCacheService;

    public CurrencyConversionServiceImpl(CurrencyCacheService currencyCacheService) {
        this.currencyCacheService = currencyCacheService;
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
            FxQuote quote = YahooFinance.getFx(currencyPair);
            if (quote == null || quote.getPrice() == null) {
                throw new CurrencyConversionException("No exchange rate available for " + currencyPair);
            }

            currencyCacheService.put(currencyPair, quote.getPrice());

            return quote.getPrice();
        } catch (Exception e) {
            throw new CurrencyConversionException("Error while fetching exchange rate");
        }
    }
}
