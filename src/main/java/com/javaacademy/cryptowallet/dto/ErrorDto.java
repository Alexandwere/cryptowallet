package com.javaacademy.cryptowallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ошибка")
public class ErrorDto {
    @Schema(description = "Код ошибки")
    private int code;
    @Schema(description = "Описание ошибка")
    private String description;
}
