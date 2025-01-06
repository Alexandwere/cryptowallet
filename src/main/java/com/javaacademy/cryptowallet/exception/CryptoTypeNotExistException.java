package com.javaacademy.cryptowallet.exception;

import lombok.experimental.StandardException;

@StandardException
public class CryptoTypeNotExistException extends RuntimeException {
    public CryptoTypeNotExistException(String message) {
        super(message);
    }
}
