package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.dto.RefillWithdrawBodyDto;
import com.javaacademy.cryptowallet.service.CryptoAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cryptowallet")
@RequiredArgsConstructor
@Tag(name = "Контроллер криптокошельков", description = "Контроллер для управления криптосчетами")
public class CryptoController {
    private final CryptoAccountService cryptoAccountService;

    @GetMapping()
    @Operation(summary = "Получение всех криптосчётов пользователя",
            description = "Получение всех криптосчетов пользователя по его логину")
    public List<CryptoAccountDto> findAll(@PathVariable @RequestParam String login) {
        return cryptoAccountService.findAllForUser(login);
    }

    @GetMapping("/balance/{id}")
    @Operation(summary = "Получение баланса счёта в рублях",
            description = "Получение баланса счёта по ID в рублях")
    public BigDecimal getBalance(@PathVariable String id) {
        return cryptoAccountService.balanceRub(UUID.fromString(id));
    }

    @GetMapping("/balance?username={login}")
    @Operation(summary = "Получение баланса всех счетов пользователя",
            description = "Получение баланса всех криптосчетов пользователя, требуется логин")
    public BigDecimal getAllBalance(@PathVariable String login) {
        return cryptoAccountService.allBalanceRub(login);
    }

    @PostMapping
    @Operation(summary = "Создание криптосчёта",
            description = "Создание крипточсёта, требуется логин и тип криптовалюты")
    public UUID createCryptoAccount(@RequestBody CryptoAccountDto accountDto) {
        return cryptoAccountService.createCryptoAccount(accountDto);
    }

    @PostMapping("/refill")
    @Operation(summary = "Пополнение счёта в рублях",
            description = "Пополнение счёта за рубли, требуется ID счёта и сумма в рублях")
    public void refill(@RequestBody RefillWithdrawBodyDto bodyDto) {
        cryptoAccountService.topUpInRub(UUID.fromString(bodyDto.getUuid()), bodyDto.getAmountRubles());
    }

    @PostMapping("/withdrawal")
    @Operation(summary = "Снятие со счёта в рублях",
            description = "Снятие со счёта в рублях, требуется ID счёта и сумма в рублях")
    public String withdraw(@RequestBody RefillWithdrawBodyDto bodyDto) {
        return cryptoAccountService.withdrawRub(UUID.fromString(bodyDto.getUuid()), bodyDto.getAmountRubles());
    }

}
