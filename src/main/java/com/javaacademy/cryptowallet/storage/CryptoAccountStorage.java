package com.javaacademy.cryptowallet.storage;

import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.exception.AccountAlreadyExistException;
import com.javaacademy.cryptowallet.exception.AccountNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CryptoAccountStorage {
    private final Map<UUID, CryptoAccount> cryptoAccountMap = new HashMap<>();

    public void save(CryptoAccount account) {
        if (cryptoAccountMap.containsKey(account.getUuid())) {
            throw new AccountAlreadyExistException("Аккаунт уже существует.");
        }
        cryptoAccountMap.put(account.getUuid(), account);
    }

    public CryptoAccount findAccount(UUID uuid) {
        if (cryptoAccountMap.containsKey(uuid)) {
            return cryptoAccountMap.get(uuid);
        }
        throw new AccountNotFoundException("Аккаунт не найден");
    }

    public List<CryptoAccount> findAllFofUser(String login) {
        return cryptoAccountMap.values().stream()
                .filter(e -> e.getLogin().equalsIgnoreCase(login))
                .toList();
    }
}
