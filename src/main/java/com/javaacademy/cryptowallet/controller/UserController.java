package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.ResetPasswordDto;
import com.javaacademy.cryptowallet.dto.UserDto;
import com.javaacademy.cryptowallet.service.CryptoAccountService;
import com.javaacademy.cryptowallet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cryptowallet/user")
@RequiredArgsConstructor
@Tag(name = "Контроллер пользователей", description = "Контроллеры для управления пользователями")
public class UserController {
    private final UserService userService;
    private final CryptoAccountService cryptoAccountService;

    @PostMapping("/signup")
    @Operation(summary = "Управление пользователями", description = "Регистрация пользователя")
    public void save(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Управление пользователями", description = "Смена пароля")
    public void resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        userService.resetPassword(resetPasswordDto);
    }

}
