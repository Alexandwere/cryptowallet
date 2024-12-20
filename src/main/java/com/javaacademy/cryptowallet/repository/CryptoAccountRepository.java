package com.javaacademy.cryptowallet.repository;

import com.javaacademy.cryptowallet.entity.CryptoAccount;
import com.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CryptoAccountRepository {
    private final CryptoAccountStorage cryptoAccountStorage;

    public CryptoAccount findAccount(UUID uuid) {
        return cryptoAccountStorage.findAccount(uuid);
    }

    public List<CryptoAccount> findAllForUser(String login) {
        return cryptoAccountStorage.findAllFofUser(login);
    }

    public void saveCryptoAccount(CryptoAccount account) {
        cryptoAccountStorage.save(account);
    }
}
