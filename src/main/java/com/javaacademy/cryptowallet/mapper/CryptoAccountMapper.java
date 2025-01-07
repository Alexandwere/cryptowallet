package com.javaacademy.cryptowallet.mapper;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.entity.CoinType;
import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.exception.CryptoTypeNotExistException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CryptoAccountMapper {

    public CryptoAccountDto convertToDto(CryptoAccount account) {
        return new CryptoAccountDto(account.getLogin(), account.getCoinType().getDescription());
    }

    public CryptoAccount convertToAccount(CryptoAccountDto accountDto) {
        Optional<CoinType> findCoin = Arrays.stream(CoinType.values())
                .filter(e -> e.getDescription().equalsIgnoreCase(accountDto.getCoinType())).findFirst();
        if (findCoin.isEmpty()) {
            throw new CryptoTypeNotExistException("Данный тип криптовалюты не поддерживается.");
        }
        return new CryptoAccount(accountDto.getLogin(), findCoin.get());
    }
}
