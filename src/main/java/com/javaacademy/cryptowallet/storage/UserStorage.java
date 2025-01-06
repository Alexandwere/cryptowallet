package com.javaacademy.cryptowallet.storage;

import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.exception.UserAlreadyExistException;
import com.javaacademy.cryptowallet.exception.UserNotExistException;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserStorage {
    private final Map<String, User> dataUsers = new HashMap<>();

    public User getUserByLogin(String login) {
        if (dataUsers.containsKey(login)) {
            return dataUsers.get(login);
        }
        throw new UserNotExistException("Пользователь не существует");
    }

    public void saveUser(User user) {
        if (dataUsers.containsKey(user.getLogin())) {
            throw new UserAlreadyExistException("Пользователь с таким логином уже существует.");
        }
        dataUsers.put(user.getLogin(), user);
    }
}
