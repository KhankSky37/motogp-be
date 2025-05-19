package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.PasswordDTO;
import com.example.motogp_b.dto.UserDto;
import com.example.motogp_b.entity.User;
import com.example.motogp_b.repository.UserRepository;
import com.example.motogp_b.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    ModelMapper modelMapper;

    @Override
    public UserDto login(UserDto userDto) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found with email: " + userDto.getEmail());
        }

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public String register(UserDto userDto) {
        if (userDto.getPassword() == null || userDto.getConfirmPassword() == null || userDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password and confirm password cannot be empty");
        }
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }
        User user = modelMapper.map(userDto, User.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole("USER");
        User savedUser = userRepository.save(user);
        return "User registered successfully with ID: " + savedUser.getId();
    }

    @Override
    public List<UserDto> findAll(UserDto userDto) {
        return userRepository.findAllUsers(userDto).stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public UserDto findById(String id) {
        return modelMapper.map(userRepository.findById(id), UserDto.class);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public UserDto update(String id, UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setId(id);
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public void updatePassword(String id, PasswordDTO passwordDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password");
        }

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        if (passwordDTO.getNewPassword().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }
}