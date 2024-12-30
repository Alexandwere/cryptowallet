package com.javaacademy.cryptowallet.mapper;

import com.javaacademy.cryptowallet.dto.UserDto;
import com.javaacademy.cryptowallet.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public User convertToUser(UserDto userDto) {
        return new User(userDto.getLogin(), userDto.getEmail(), userDto.getPassword());
    }

    public UserDto convertToUserDto(User user) {
        return UserDto.builder().login(user.getLogin()).build();
    }
}
