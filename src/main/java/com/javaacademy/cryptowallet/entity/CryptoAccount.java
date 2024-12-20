package com.javaacademy.cryptowallet.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CryptoAccount {
    private String login;
    private CoinType coinType;
    private BigDecimal valueCoin = BigDecimal.ZERO;
    private UUID uuid = UUID.randomUUID();

    public CryptoAccount(String login, CoinType coinType) {
        this.login = login;
        this.coinType = coinType;
    }
}