package com.javaacademy.cryptowallet.service.conversionService;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Profile("local")
public class LocalConversionRubUsdServiceImp implements ConversionRubUsdService {
    @Value("${app.exchange.standard-usd-cost}")
    private BigDecimal standardPrice;

    public BigDecimal convertToRub(@NonNull BigDecimal countUsd) {
        return countUsd.multiply(standardPrice);
    }

    @Override
    public BigDecimal convertToUsd(@NonNull BigDecimal countRub) {
        return countRub.divide(standardPrice);
    }
}
