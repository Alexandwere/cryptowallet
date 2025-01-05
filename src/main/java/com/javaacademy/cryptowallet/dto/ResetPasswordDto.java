package com.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Тело для смены пароля у пользователя")
public class ResetPasswordDto {
    @Schema(description = "Логин")
    private String login;
    @Schema(description = "Старый пароль")
    @JsonProperty("old_pass")
    private String oldPass;
    @Schema(description = "Новый пароль")
    @JsonProperty("new_pass")
    private String newPass;

}
