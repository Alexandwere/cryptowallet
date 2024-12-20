package com.javaacademy.cryptowallet.service.util;

import com.javaacademy.cryptowallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {
    private static UserService userService;

    @Autowired
    public UserUtil(UserService userService) {
        UserUtil.userService = userService;
    }

    public static void checkUserPresence(String login) {
        if (userService.getUserByLogin(login) == null) {
            throw new RuntimeException("Пользователя с таким логином не существует");
        }
    }
}
