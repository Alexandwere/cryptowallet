package com.javaacademy.cryptowallet.service.conversionService;

import com.javaacademy.cryptowallet.entity.CoinType;

import java.math.BigDecimal;

public interface ConversionCoinService {
    public BigDecimal costCoin(CoinType coinType);
}