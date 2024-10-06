package com.estifie.expensetracker.exception.auth;

import com.estifie.expensetracker.exception.global.UnauthorizedException;

public class AuthorizationException extends UnauthorizedException {
    private static final String DEFAULT_MESSAGE = "Not authorized";

    public AuthorizationException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
