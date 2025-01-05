package com.javaacademy.cryptowallet.service;

import com.javaacademy.cryptowallet.dto.ResetPasswordDto;
import com.javaacademy.cryptowallet.dto.UserDto;
import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.mapper.UserMapper;
import com.javaacademy.cryptowallet.repository.UserRepository;
import com.javaacademy.cryptowallet.service.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void saveUser(UserDto userDto) {
        userRepository.saveUser(userMapper.convertToUser(userDto));
    }

    public UserDto getUserByLogin(String login) {
        try {
            return userMapper.convertToUserDto(userRepository.getUserByLogin(login));
        } catch (Exception e) {
            return UserDto.builder().login("Пользователь не существует").build();
        }
    }

    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        User user = userRepository.getUserByLogin(resetPasswordDto.getLogin());
        if (user.getPassword().equals(resetPasswordDto.getOldPass())) {
            user.setPassword(resetPasswordDto.getNewPass());
        } else throw new RuntimeException("Неправильный пароль. Изменить невозможно.");
    }

}
