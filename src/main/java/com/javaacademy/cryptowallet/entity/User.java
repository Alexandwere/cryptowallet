package com.javaacademy.cryptowallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String login;
    private String email;
    private String password;
}
