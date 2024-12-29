package com.javaacademy.cryptowallet.service.conversionService;

import com.javaacademy.cryptowallet.entity.CoinType;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Profile("local")
public class LocalConversionCoinServiceImp implements ConversionCoinService {
    @Value("app.exchange.standard-coin-cost")
    private BigDecimal standardCost;

    @SneakyThrows
    public BigDecimal costCoin(CoinType coinType) {
        return standardCost;
    }
}
