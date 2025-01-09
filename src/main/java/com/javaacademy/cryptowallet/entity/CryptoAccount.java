package com.javaacademy.cryptowallet.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CryptoAccount {
    private String login;
    private CoinType coinType;
    private BigDecimal balanceCoin;
    private UUID uuid;

    public CryptoAccount(String login, CoinType coinType) {
        this.login = login;
        this.coinType = coinType;
    }
}
