package com.javaacademy.cryptowallet.entity;

import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CryptoAccount {
    @NonNull
    private String login;
    @NonNull
    private CoinType coinType;
    private BigDecimal balanceCoin;
    private UUID uuid;

    public CryptoAccount(@NotNull String login, @NotNull CoinType coinType) {
        this.login = login;
        this.coinType = coinType;
    }
}