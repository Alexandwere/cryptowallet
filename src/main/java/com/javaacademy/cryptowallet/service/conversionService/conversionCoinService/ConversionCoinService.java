package com.javaacademy.cryptowallet.service.conversionService.conversionCoinService;

import com.javaacademy.cryptowallet.entity.CoinType;

import java.math.BigDecimal;

public interface ConversionCoinService {
    BigDecimal costCoin(CoinType coinType);
}
