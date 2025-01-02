package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefillWithdrawBodyDto {
    @Schema(description = "Уникальный номер пользователя")
    @JsonProperty("account_id")
    private String uuid;
    @Schema(description = "Сумма в рублях")
    @JsonProperty("amount_rubles")
    private BigDecimal amountRubles;
}
