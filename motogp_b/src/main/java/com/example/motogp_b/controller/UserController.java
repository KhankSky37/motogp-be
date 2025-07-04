package com.example.motogp_b.controller;

import com.example.motogp_b.dto.ForgotPasswordRequestDto;
import com.example.motogp_b.dto.PasswordDTO;
import com.example.motogp_b.dto.ResetPasswordRequestDto;
import com.example.motogp_b.dto.UserDto;
import com.example.motogp_b.service.EmailService;
import com.example.motogp_b.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.register(userDto));
    }

    @GetMapping
    ResponseEntity<List<UserDto>> getUsers(UserDto userDto) {
        return ResponseEntity.ok(userService.findAll(userDto));
    }

    @PostMapping
    ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.create(userDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<UserDto> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<UserDto> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.update(id, userDto));
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<String> changePasswordUser(@PathVariable("id") String id, @RequestBody PasswordDTO passwordDTO) {
        try {
            userService.updatePassword(id, passwordDTO);
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) {
        try {
            String message = userService.processForgotPassword(forgotPasswordRequestDto);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        try {
            String message = userService.resetPassword(resetPasswordRequestDto);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}