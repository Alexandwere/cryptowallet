package com.javaacademy.cryptowallet.config;

import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = ConversionProperties.class)
public class ConversionConfig {

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

}
