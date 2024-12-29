package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.entity.CoinType;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

@Profile("local")
public class LocalConversionService {
    @Value("app.exchange.standard-coin-cost")
    private BigDecimal standardCost;

    @SneakyThrows
    public BigDecimal costCoin(CoinType coinType) {
        return standardCost;
    }
}
