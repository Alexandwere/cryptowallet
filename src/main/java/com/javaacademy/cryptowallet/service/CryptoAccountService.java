package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.mapper.CryptoAccountMapper;
import com.javaacademy.cryptowallet.repository.CryptoAccountRepository;
import com.javaacademy.cryptowallet.service.conversionService.ConversionCoinService;
import com.javaacademy.cryptowallet.service.conversionService.ConversionRubUsdService;
import com.javaacademy.cryptowallet.service.util.UserUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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

    /**
     Поиск всех криптосчетов пользователя
     */
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

    /**
     Пополнение счёта в рублях
     */
    public void topUpInRub(UUID uuid, BigDecimal countRub) {
        CryptoAccount account = cryptoAccountRepository.findAccount(uuid);
        account.setValueCoin(account.getValueCoin().add(valueCoin(account, countRub)));
    }

    /**
     Снять рубли со счёта
     */
    public String withdrawRub(UUID uuid, BigDecimal countRub) {
        CryptoAccount account = cryptoAccountRepository.findAccount(uuid);
        BigDecimal countCoin = valueCoin(account, countRub);
        if (rubUsdService.convertToUsd(countRub).compareTo(countCoin) < 0) {
            throw new RuntimeException("Операция отклонена, на счёте недостаточно средств");
        }
        account.setValueCoin(account.getValueCoin().subtract(valueCoin(account, countRub)));
        return String.format("Операция прошла успешно. Продано %s %s.", countCoin, account.getValueCoin());
    }

    /**
     Получение баланса в рублях (добавить проверку на отрицательный баланс?)
     */
    public BigDecimal balanceRub(@NonNull UUID uuid) {
        CryptoAccount account = cryptoAccountRepository.findAccount(uuid);
        BigDecimal coinUsdPrice = coinService.costCoin(account.getCoinType());
        BigDecimal balanceInUsd = account.getValueCoin().multiply(coinUsdPrice);
        return rubUsdService.convertToRub(balanceInUsd);
    }

    /**
     Получение баланса всех счетов пользователя
     */
    public BigDecimal allBalanceRub(@NonNull String login) {
        UserUtil.checkUserPresence(login);
        List<CryptoAccount> accountsByLogin = cryptoAccountRepository.findAllForUser(login);
        AtomicReference<BigDecimal> balanceCoin = new AtomicReference<>(BigDecimal.ZERO);
        accountsByLogin.stream().forEach(e -> balanceCoin.set(balanceCoin.get().add(balanceRub(e.getUuid()))));
        return balanceCoin.get();
    }

    /**
     Получение количества криптовалюты за рубли
     */
    private BigDecimal valueCoin(CryptoAccount account, BigDecimal countRub) {
        BigDecimal coinUsdPrice = coinService.costCoin(account.getCoinType());
        BigDecimal dollars = rubUsdService.convertToUsd(countRub);
        return dollars.divide(coinUsdPrice);
    }

}
