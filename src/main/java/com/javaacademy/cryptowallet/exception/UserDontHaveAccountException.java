package com.javaacademy.cryptowallet.exception;

import lombok.experimental.StandardException;

@StandardException
public class UserDontHaveAccountException extends RuntimeException {
    public UserDontHaveAccountException(String message) {
        super(message);
    }
}
