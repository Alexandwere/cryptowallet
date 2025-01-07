package com.javaacademy.cryptowallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@Schema(description = "Пользователь")
public class UserDto {
    @Schema(description = "Логин")
    @NonNull
    private String login;
    @Schema(description = "Эл. почта")
    private String email;
    @Schema(description = "Пароль")
    private String password;
}
