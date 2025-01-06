package com.javaacademy.cryptowallet.exception;

import lombok.experimental.StandardException;

@StandardException
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
