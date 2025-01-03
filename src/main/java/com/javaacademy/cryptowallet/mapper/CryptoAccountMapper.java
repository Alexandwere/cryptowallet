package com.javaacademy.cryptowallet.mapper;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.entity.CoinType;
import com.javaacademy.cryptowallet.entity.CryptoAccount;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CryptoAccountMapper {

    public CryptoAccountDto convertToDto(CryptoAccount account) {
        return new CryptoAccountDto(account.getLogin(), account.getCoinType().getDescription());
    }

    public CryptoAccount convertToAccount(CryptoAccountDto accountDto) {
        if (Arrays.stream(CoinType.values())
                .noneMatch(e -> e.getDescription().equalsIgnoreCase(accountDto.getCoinType()))) {
            throw new RuntimeException("Данный тип криптовалюты не поддерживается.");
        }
        Optional<CoinType> findCoin = Arrays.stream(CoinType.values())
                .filter(e -> e.getDescription().equalsIgnoreCase(accountDto.getCoinType())).findFirst();

        return new CryptoAccount(accountDto.getLogin(), findCoin.get());
    }
}
