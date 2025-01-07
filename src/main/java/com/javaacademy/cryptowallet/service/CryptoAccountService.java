package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.exception.IncorrectAmountException;
import com.javaacademy.cryptowallet.exception.UserDontHaveAccountException;
import com.javaacademy.cryptowallet.exception.UserNotExistException;
import com.javaacademy.cryptowallet.mapper.CryptoAccountMapper;
import com.javaacademy.cryptowallet.repository.CryptoAccountRepository;
import com.javaacademy.cryptowallet.service.conversionService.ConversionCoinService;
import com.javaacademy.cryptowallet.service.conversionService.ConversionRubUsdService;
import com.javaacademy.cryptowallet.service.util.UserUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class CryptoAccountService {
    private final CryptoAccountRepository cryptoAccountRepository;
    private final CryptoAccountMapper cryptoAccountMapper;
    private final ConversionCoinService conversionCoinService;
    private final ConversionRubUsdService conversionRubUsdService;

    /**
     Поиск всех криптосчетов пользователя
     */
    public List<CryptoAccountDto> findAllForUser(String login) {
        try {
            UserUtil.checkUserPresence(login);
        } catch (UserNotExistException e) {
            return new ArrayList<>(List.of(new CryptoAccountDto(e.getMessage(), "")));
        }
        List<CryptoAccountDto> result = new ArrayList<>(cryptoAccountRepository.findAllForUser(login).stream()
                .map(cryptoAccountMapper::convertToDto).toList());
        if (result.isEmpty()) {
            CryptoAccountDto notExistCryptoAccount = new CryptoAccountDto("Счетов у пользователя %s не обнаружено"
                    .formatted(login), "");
            result.add(notExistCryptoAccount);
        }
        return result;
    }

    /**
     Создание криптосчёта
     */
    public UUID createCryptoAccount(@NonNull CryptoAccountDto cryptoAccountDto) {
        UserUtil.checkUserPresence(cryptoAccountDto.getLogin());
        CryptoAccount account = cryptoAccountMapper.convertToAccount(cryptoAccountDto);
        cryptoAccountRepository.saveCryptoAccount(account);
        return account.getUuid();
    }

    /**
     Пополнение счёта в рублях
     */
    public void topUpInRub(UUID uuid, BigDecimal countRub) {
        checkAmount(countRub);
        CryptoAccount account = cryptoAccountRepository.findAccount(uuid);
        account.setBalanceCoin(account.getBalanceCoin().add(valueCoin(account, countRub)));
    }

    /**
     Снять рубли со счёта
     */
    public String withdrawRub(UUID uuid, BigDecimal countRub) {
        try {
            checkAmount(countRub);
        } catch (IncorrectAmountException e) {
            return e.getMessage();
        }
        CryptoAccount account = cryptoAccountRepository.findAccount(uuid);
        BigDecimal countWithdrawalCoin = valueCoin(account, countRub);
        if (account.getBalanceCoin().compareTo(countWithdrawalCoin) < 0) {
            return "Операция отклонена, на счёте недостаточно средств";
        }
        account.setBalanceCoin(account.getBalanceCoin().subtract(valueCoin(account, countRub)));
        return String.format("Операция прошла успешно. Продано %s %s.", countWithdrawalCoin, account.getCoinType());
    }

    /**
     Получение баланса в рублях
     */
    public BigDecimal balanceRub(@NonNull UUID uuid) {
        CryptoAccount account = cryptoAccountRepository.findAccount(uuid);
        BigDecimal coinUsdPrice = conversionCoinService.costCoin(account.getCoinType());
        BigDecimal balanceInUsd = account.getBalanceCoin().multiply(coinUsdPrice);
        return conversionRubUsdService.convertToRub(balanceInUsd);
    }

    /**
     Получение баланса всех счетов пользователя
     */
    public BigDecimal allBalanceRub(@NonNull String login) {
        UserUtil.checkUserPresence(login);
        List<CryptoAccount> accountsByLogin = cryptoAccountRepository.findAllForUser(login);
        if (accountsByLogin.isEmpty()) {
            throw new UserDontHaveAccountException("Счетов у пользователя %s не обнаружено"
                    .formatted(login));
        }
        AtomicReference<BigDecimal> balanceCoin = new AtomicReference<>(BigDecimal.ZERO);
        accountsByLogin.forEach(e -> balanceCoin.set(balanceCoin.get().add(balanceRub(e.getUuid()))));
        return balanceCoin.get();
    }

    /**
     Получение количества криптовалюты за рубли
     */
    private BigDecimal valueCoin(CryptoAccount account, BigDecimal countRub) {
        BigDecimal coinUsdPrice = conversionCoinService.costCoin(account.getCoinType());
        BigDecimal dollarsOnAccount = conversionRubUsdService.convertToUsd(countRub);
        return dollarsOnAccount.divide(coinUsdPrice);
    }

    /**
     Проверка на отрицательную сумму денег
     */
    private void checkAmount(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IncorrectAmountException("Нельзя ввести отрицательную сумму");
        }
    }

}
