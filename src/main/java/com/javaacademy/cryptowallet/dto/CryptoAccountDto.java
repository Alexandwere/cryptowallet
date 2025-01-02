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
    @Schema(description = "логин")
    @JsonProperty("username")
    private String login;
    @Schema(description = "Тип криптовалюты")
    @JsonProperty("crypto_type")
    private String coinType;
}
