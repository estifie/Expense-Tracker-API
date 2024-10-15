package com.estifie.expensetracker.exception.tag;

import com.estifie.expensetracker.exception.global.NotFoundException;

public class TagNotFoundException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "Tag not found";

    public TagNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public TagNotFoundException(String message) {
        super(message);
    }
}
