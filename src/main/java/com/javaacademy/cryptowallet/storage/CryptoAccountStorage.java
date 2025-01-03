package com.javaacademy.cryptowallet.storage;

import com.javaacademy.cryptowallet.entity.CryptoAccount;
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
            throw new RuntimeException("Аккаунт уже существует.");
        }
        cryptoAccountMap.put(account.getUuid(), account);
    }

    public CryptoAccount findAccount(UUID uuid) {
        if (!cryptoAccountMap.containsKey(uuid)) {
            throw new RuntimeException("Аккаунт не найден");
        }
        return cryptoAccountMap.get(uuid);
    }

    public List<CryptoAccount> findAllFofUser(String login) {
        return cryptoAccountMap.values().stream()
                .filter(e -> e.getLogin().equalsIgnoreCase(login))
                .toList();
    }
}
