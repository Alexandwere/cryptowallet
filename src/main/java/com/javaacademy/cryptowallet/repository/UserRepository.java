package com.javaacademy.cryptowallet.repository;

import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepository {
    private final UserStorage userStorage;

    public void saveUser(User user) {
        userStorage.saveUser(user);
    }

    public User getUserByLogin(String login) {
        return userStorage.getByLogin(login);
    }
}
