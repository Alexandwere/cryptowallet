package com.javaacademy.cryptowallet.service.conversionService;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.NonNull;
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
public class ConversionRubUsdServiceImp implements ConversionRubUsdService {
    private static final String CRB_URL = "https://www.cbr-xml-daily.ru/latest.js";
    private static final String RATES = "rates";
    private static final String CURRENCY = "USD";

    private OkHttpClient client;

    @SneakyThrows
    public BigDecimal convertToRub(@NonNull BigDecimal countUsd) {
        return countUsd.divide(getRate());
    }

    @SneakyThrows
    public BigDecimal convertToUsd(@NonNull BigDecimal countRub) {
        return countRub.multiply(getRate());
    }

    @SneakyThrows
    private BigDecimal getRate() {
        Request request = new Request.Builder()
                .url(CRB_URL)
                .get()
                .build();
        @Cleanup Response response = client.newCall(request).execute();

        if (response.isSuccessful() && !response.body().string().isEmpty()) {
            return JsonPath.parse(response.body().string())
                    .read(JsonPath.compile(String.format("$['%s'], ['%s']", RATES, CURRENCY)), BigDecimal.class);
        }
        throw new RuntimeException("Ответ не получен");
    }
}
