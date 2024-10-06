package com.estifie.expensetracker.exception.validation;

import com.estifie.expensetracker.exception.global.BadRequestException;

public class InvalidCredentialsException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "Invalid credentials";

    public InvalidCredentialsException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
