package com.javaacademy.cryptowallet.service.userService;

import com.javaacademy.cryptowallet.dto.ResetPasswordDto;
import com.javaacademy.cryptowallet.dto.UserDto;

public interface UserService {
    void saveUser(UserDto userDto);
    UserDto getUserByLogin(String login);
    void resetPassword(ResetPasswordDto resetPasswordDto);
}
