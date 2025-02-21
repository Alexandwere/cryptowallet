package com.javaacademy.cryptowallet.service.cryptoAccountService;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.dto.OperationMoneyBodyDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CryptoAccountService {
    List<CryptoAccountDto> findAllForUser(String login);
    UUID createCryptoAccount(CryptoAccountDto cryptoAccountDto);
    void topUpInRub(OperationMoneyBodyDto bodyDto);
    String withdrawRub(OperationMoneyBodyDto bodyDto);
    BigDecimal balanceRub(UUID uuid);
    BigDecimal allBalanceRub(String login);
}
