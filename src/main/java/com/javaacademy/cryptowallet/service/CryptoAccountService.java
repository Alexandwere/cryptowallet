package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.mapper.CryptoAccountMapper;
import com.javaacademy.cryptowallet.repository.CryptoAccountRepository;
import com.javaacademy.cryptowallet.service.conversionService.ConversionCoinService;
import com.javaacademy.cryptowallet.service.conversionService.ConversionRubUsdService;
import com.javaacademy.cryptowallet.service.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CryptoAccountService {
    private final CryptoAccountRepository cryptoAccountRepository;
    private final CryptoAccountMapper cryptoAccountMapper;
    private final UserService userService;
    private final ConversionCoinService coinService;
    private final ConversionRubUsdService rubUsdService;

    public CryptoAccountDto findAccount(UUID uuid) {
        return cryptoAccountMapper.convertToDto(cryptoAccountRepository.findAccount(uuid));
    }

    public List<CryptoAccountDto> findAllForUser(String login) {
        UserUtil.checkUserPresence(login);
        return cryptoAccountRepository.findAllForUser(login).stream()
                .map(cryptoAccountMapper::convertToDto).toList();
    }

    public UUID createCryptoAccount(CryptoAccountDto cryptoAccountDto) {
        UserUtil.checkUserPresence(cryptoAccountDto.getLogin());
        cryptoAccountRepository.saveCryptoAccount(cryptoAccountMapper.convertToAccount(cryptoAccountDto));
        return cryptoAccountMapper.convertToAccount(cryptoAccountDto).getUuid();
    }

    public void topUpInRub(UUID uuid, BigDecimal countRub) {
        CryptoAccount account = cryptoAccountRepository.findAccount(uuid);
        account.setValueCoin(account.getValueCoin().add(coinCost(account, countRub)));
    }

    public String withdrawRub(UUID uuid, BigDecimal countRub) {
        CryptoAccount account = cryptoAccountRepository.findAccount(uuid);
        BigDecimal costCoin = coinCost(account, countRub);
        if (rubUsdService.convertToUsd(countRub).compareTo(costCoin) < 0) {
            throw new RuntimeException("Операция отклонена, на счёте недостаточно средств");
        }
        account.setValueCoin(account.getValueCoin().subtract(coinCost(account, countRub)));
        return String.format("Операция прошла успешно. Продано %s %s.", costCoin, account.getValueCoin());
    }

    private BigDecimal coinCost(CryptoAccount account, BigDecimal countRub) {
        BigDecimal coinPrice = coinService.costCoin(account.getCoinType());
        BigDecimal dollars = rubUsdService.convertToUsd(countRub);
        return dollars.divide(coinPrice);
    }
}
