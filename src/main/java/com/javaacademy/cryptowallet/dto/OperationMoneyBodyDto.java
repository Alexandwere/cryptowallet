package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Тело для пополнения и снятия в рублях")
public class OperationMoneyBodyDto {
    @Schema(description = "Уникальный номер пользователя")
    @JsonProperty("account_id")
    @NonNull
    private String uuid;
    @Schema(description = "Сумма в рублях")
    @JsonProperty("amount_rubles")
    @NonNull
    private BigDecimal amountRubles;
}
