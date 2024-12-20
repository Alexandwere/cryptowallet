package com.javaacademy.cryptowallet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CoinType {
    BTC("bitcoin"), ETH("ethereum"), SOL("solana");

    private final String description;
}
