package com.javaacademy.cryptowallet.service.cryptoAccountService;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;

import java.util.List;
import java.util.UUID;

public interface CryptoAccountService {
    List<CryptoAccountDto> findAllForUser(String login);
    UUID createCryptoAccount(CryptoAccountDto cryptoAccountDto);
}
