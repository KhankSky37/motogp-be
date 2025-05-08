package com.example.motogp_b.service;

import com.example.motogp_b.dto.PasswordDTO;
import com.example.motogp_b.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll(UserDto userDto);

    UserDto findById(String id);

    UserDto create(UserDto userDto);

    UserDto update(String id, UserDto userDto);

    void updatePassword(String id, PasswordDTO passwordDTO);

    void deleteById(String id);
}