package com.javaacademy.cryptowallet.controller;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.service.CryptoAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
