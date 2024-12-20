package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.dto.CryptoAccountDto;
import com.javaacademy.cryptowallet.mapper.CryptoAccountMapper;
import com.javaacademy.cryptowallet.repository.CryptoAccountRepository;
import com.javaacademy.cryptowallet.service.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CryptoAccountService {
    private final CryptoAccountRepository cryptoAccountRepository;
    private final CryptoAccountMapper cryptoAccountMapper;
    private final UserService userService;

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

}
