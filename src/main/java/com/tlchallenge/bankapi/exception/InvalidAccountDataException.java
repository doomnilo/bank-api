package com.tlchallenge.bankapi.exception;

public class InvalidAccountDataException extends RuntimeException {
    private final String field;

    public InvalidAccountDataException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
