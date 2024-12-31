package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoAccountDto {
    @Schema(name = "Логин", description = "логин")
    @JsonProperty("username")
    private String login;
    @JsonProperty("crypto_type")
    private String coinType;
}
