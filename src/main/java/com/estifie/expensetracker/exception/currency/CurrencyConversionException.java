package com.estifie.expensetracker.exception.currency;

import com.estifie.expensetracker.exception.global.InternalServerException;

public class CurrencyConversionException extends InternalServerException {
    private static final String DEFAULT_MESSAGE = "User not found";

    public CurrencyConversionException() {
        super(DEFAULT_MESSAGE);
    }

    public CurrencyConversionException(String message) {
        super(message);
    }
}
