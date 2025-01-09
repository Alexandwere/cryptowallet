package com.javaacademy.cryptowallet.service.userService;

import com.javaacademy.cryptowallet.dto.ResetPasswordDto;
import com.javaacademy.cryptowallet.dto.UserDto;
import com.javaacademy.cryptowallet.entity.User;
import com.javaacademy.cryptowallet.mapper.UserMapper;
import com.javaacademy.cryptowallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void saveUser(UserDto userDto) {
        if (userDto.getPassword() == null || userDto.getEmail() == null) {
            throw new RuntimeException("Отсутствует почта или пароль");
        }
        userRepository.saveUser(userMapper.convertToUser(userDto));
    }

    public UserDto getUserByLogin(String login) {
        return userMapper.convertToUserDto(userRepository.getUserByLogin(login));
    }

    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        User user = userRepository.getUserByLogin(resetPasswordDto.getLogin());
        if (user.getPassword().equals(resetPasswordDto.getOldPass())) {
            user.setPassword(resetPasswordDto.getNewPass());
        } else {
            throw new RuntimeException("Неправильный пароль. Изменить невозможно.");
        }
    }
}
