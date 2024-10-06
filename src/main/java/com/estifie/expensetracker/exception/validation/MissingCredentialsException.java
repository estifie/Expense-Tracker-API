package com.estifie.expensetracker.exception.validation;

import com.estifie.expensetracker.exception.global.BadRequestException;

public class MissingCredentialsException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "Missing credentials";

    public MissingCredentialsException() {
        super(DEFAULT_MESSAGE);
    }

    public MissingCredentialsException(String message) {
        super(message);
    }
}
