package com.javaacademy.cryptowallet.exception;

import lombok.experimental.StandardException;

@StandardException
public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException(String message) {
        super(message);
    }
}
