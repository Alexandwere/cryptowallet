package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefillWithdrawBodyDto {
    @JsonProperty("account_id")
    private String uuid;
    @JsonProperty("amount_rubles")
    private BigDecimal amountRubles;
}
