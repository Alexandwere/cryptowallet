package com.javaacademy.cryptowallet.exception;

import lombok.experimental.StandardException;

@StandardException
public class IncorrectAmountException extends RuntimeException {
    public IncorrectAmountException(String message) {
        super(message);
    }
}
