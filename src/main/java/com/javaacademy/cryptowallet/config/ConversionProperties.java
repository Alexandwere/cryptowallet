package com.javaacademy.cryptowallet.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app")
@Setter
@Getter
public class ConversionProperties {
    @JsonProperty("site-api")
    private String siteApi;
    @JsonProperty("header-key")
    private String headerKey;
    @JsonProperty("header-value")
    private String headerValue;

}
