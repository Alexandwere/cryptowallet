package com.javaacademy.cryptowallet.service.conversionService;

import lombok.NonNull;

import java.math.BigDecimal;

public interface ConversionRubUsdService {
    BigDecimal convertToRub(@NonNull BigDecimal countUsd);
    BigDecimal convertToUsd(@NonNull BigDecimal countRub);
}
