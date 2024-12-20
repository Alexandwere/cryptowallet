package com.javaacademy.cryptowallet.storage;

import com.javaacademy.cryptowallet.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserStorage {
    private final Map<String, User> dataUsers = new HashMap<>();

    public User getByLogin(String login) {
        return dataUsers.get(login);
    }

    public void saveUser(User user) {
        if (dataUsers.containsKey(user.getLogin())) {
            throw new RuntimeException("Пользователь с таким логином уже существует.");
        }
        dataUsers.put(user.getLogin(), user);
    }
}
