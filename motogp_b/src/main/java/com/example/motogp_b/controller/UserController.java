package com.example.motogp_b.controller;

import com.example.motogp_b.dto.UserDto;
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

    @GetMapping
    ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
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

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}