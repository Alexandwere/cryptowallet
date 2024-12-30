package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.dto.RefillWithdrawBodyDto;
import com.javaacademy.cryptowallet.service.CryptoAccountService;
import lombok.RequiredArgsConstructor;
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
public class CryptoController {
    private final CryptoAccountService cryptoAccountService;

    @PostMapping
    public UUID createCryptoAccount(@RequestBody CryptoAccountDto accountDto) {
        return cryptoAccountService.createCryptoAccount(accountDto);
    }

    @GetMapping()
    public List<CryptoAccountDto> findAll(@PathVariable @RequestParam String login) {
        return cryptoAccountService.findAllForUser(login);
    }

    @PostMapping("/refill")
    public void refill(@RequestBody RefillWithdrawBodyDto bodyDto) {
        cryptoAccountService.topUpInRub(UUID.fromString(bodyDto.getUuid()), bodyDto.getAmountRubles());
    }

    @PostMapping("/withdrawal")
    public String withdraw(@RequestBody RefillWithdrawBodyDto bodyDto) {
        return cryptoAccountService.withdrawRub(UUID.fromString(bodyDto.getUuid()), bodyDto.getAmountRubles());
    }

    @GetMapping("/balance/{id}")
    public BigDecimal getBalance(@PathVariable @RequestParam String id) {
        return cryptoAccountService.balanceRub(UUID.fromString(id));
    }

    @GetMapping("/balance?username={login}")
    public BigDecimal getAllBalance(@RequestParam String login) {
        return cryptoAccountService.allBalanceRub(login);
    }
}
