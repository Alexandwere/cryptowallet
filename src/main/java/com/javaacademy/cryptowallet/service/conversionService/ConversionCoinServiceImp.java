package com.javaacademy.cryptowallet.service.conversionService;

import com.javaacademy.cryptowallet.config.ConversionProperties;
import com.javaacademy.cryptowallet.entity.CoinType;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Profile("prod")
@AllArgsConstructor
public class ConversionCoinServiceImp implements ConversionCoinService {
    private static final String CURRENCY = "usd";
    private static final String REQUEST_PART = "/simple/price?ids=";
    private static final String CURRENCY_PARAMETER = "&vs_currencies=usd";

    private OkHttpClient client;
    private ConversionProperties properties;

    @SneakyThrows
    public BigDecimal costCoin(CoinType coinType) {
        Request request = new Request.Builder()
                .url(properties.getSiteApi() + REQUEST_PART + coinType.getDescription() + CURRENCY_PARAMETER)
                .get()
                .addHeader(properties.getHeaderKey(), properties.getHeaderValue())
                .build();

        @Cleanup Response response = client.newCall(request).execute();
        if (response.isSuccessful() && !response.body().string().isEmpty()) {
            return JsonPath.parse(response.body().string())
                    .read(JsonPath.compile(String.format("$['%s']['%s']", coinType.getDescription(), CURRENCY)),
                            BigDecimal.class);
        }
        throw new RuntimeException("Ответ не получен");
    }

}
