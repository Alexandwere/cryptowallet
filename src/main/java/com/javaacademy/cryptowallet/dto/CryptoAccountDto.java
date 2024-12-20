package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoAccountDto {
    @JsonProperty("username")
    private String login;
    @JsonProperty("crypto_type")
    private String coinType;
}
