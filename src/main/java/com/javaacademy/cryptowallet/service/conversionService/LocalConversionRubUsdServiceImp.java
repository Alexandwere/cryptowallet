package com.javaacademy.cryptowallet.service.conversionService;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Profile("local")
public class LocalConversionRubUsdServiceImp {
    @Value("app.exchange.standard-usd-cost")
    private BigDecimal standardPrice;

    public BigDecimal convertToRub(@NonNull BigDecimal countUsd) {
        return countUsd.divide(standardPrice);
    }
}
