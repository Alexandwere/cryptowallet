package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.ResetPasswordDto;
import com.javaacademy.cryptowallet.dto.UserDto;
import com.javaacademy.cryptowallet.service.userService.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cryptowallet/user")
@RequiredArgsConstructor
@Tag(name = "Контроллер пользователей", description = "Контроллеры для управления пользователями")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Регистрация пользователя",
            description = "Регистрация пользователя, требуется логин, эл. почта и пароль")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Успешная регистрация пользователя"),
            @ApiResponse(responseCode = "500", description = "Отказ в регистрации, логин занят")
    })
    public void save(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
    }

    @PatchMapping("/reset-password")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Смена пароля",
            description = "Смена пароля пользователя, требуется логин, старый пароль, новый пароль")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Успешная смена пароля"),
            @ApiResponse(responseCode = "500", description = "Неверный старый пароль")
    })
    public void resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        userService.resetPassword(resetPasswordDto);
    }
}
