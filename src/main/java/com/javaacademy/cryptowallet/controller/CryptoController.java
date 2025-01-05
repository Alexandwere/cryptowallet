package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.dto.RefillWithdrawBodyDto;
import com.javaacademy.cryptowallet.service.CryptoAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cryptowallet")
@RequiredArgsConstructor
@Tag(name = "Контроллер для работы с криптосчетами", description = "Содержит комады для управления криптосчетами")
public class CryptoController {
    private final CryptoAccountService cryptoAccountService;

    @GetMapping()
    @Operation(summary = "Получение всех криптосчётов пользователя",
            description = "Получение всех криптосчетов пользователя по его логину")
    public List<CryptoAccountDto> findAll(@RequestParam String login) {
        return cryptoAccountService.findAllForUser(login);
    }

    @GetMapping("/balance/{id}")
    @Operation(summary = "Получение баланса счёта в рублях",
            description = "Получение баланса счёта по ID в рублях")
    public BigDecimal getBalance(@PathVariable String id) {
        return cryptoAccountService.balanceRub(UUID.fromString(id));
    }

    @GetMapping("/balance")
    @Operation(summary = "Получение баланса всех счетов пользователя",
            description = "Получение баланса всех криптосчетов пользователя, требуется логин")
    public BigDecimal getAllBalance(@RequestParam String login) {
        return cryptoAccountService.allBalanceRub(login);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание криптосчёта",
            description = "Создание крипточсёта, требуется логин и тип криптовалюты")
    public UUID createCryptoAccount(@RequestBody CryptoAccountDto accountDto) {
        return cryptoAccountService.createCryptoAccount(accountDto);
    }

    @PostMapping("/refill")
    @Operation(summary = "Пополнение счёта в рублях",
            description = "Пополнение счёта за рубли, требуется ID счёта и сумма в рублях")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное пополнение счета"),
            @ApiResponse(responseCode = "400", description = "Некорректный формат суммы"),
            @ApiResponse(responseCode = "500", description = "Отрицательная сумма либо не найден аккаунт")
//                            content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ErrorDto.class))
    })
    public void refill(@RequestBody RefillWithdrawBodyDto bodyDto) {
        cryptoAccountService.topUpInRub(UUID.fromString(bodyDto.getUuid()), bodyDto.getAmountRubles());
    }

    @PostMapping("/withdrawal")
    @Operation(summary = "Снятие со счёта в рублях",
            description = "Снятие со счёта в рублях, требуется ID счёта и сумма в рублях")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное снятие денег"),
            @ApiResponse(responseCode = "400", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    public String withdraw(@RequestBody RefillWithdrawBodyDto bodyDto) {
        return cryptoAccountService.withdrawRub(UUID.fromString(bodyDto.getUuid()), bodyDto.getAmountRubles());
    }

}
