package com.example.motogp_b.service;

import com.example.motogp_b.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    UserDto findById(String id);

    UserDto create(UserDto userDto);

    UserDto update(String id, UserDto userDto);

    void deleteById(String id);
}