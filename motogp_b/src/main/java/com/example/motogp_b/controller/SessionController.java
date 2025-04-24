package com.example.motogp_b.controller;

import com.example.motogp_b.dto.SessionDto;
import com.example.motogp_b.service.SessionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionController {
    SessionService sessionService;

    @GetMapping
    ResponseEntity<List<SessionDto>> getSessions() {
        return ResponseEntity.ok(sessionService.findAll());
    }

    @PostMapping
    ResponseEntity<SessionDto> createSession(@RequestBody SessionDto sessionDto) {
        return ResponseEntity.ok(sessionService.create(sessionDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<SessionDto> getSession(@PathVariable String id) {
        return ResponseEntity.ok(sessionService.findById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<SessionDto> updateSession(@PathVariable String id, @RequestBody SessionDto sessionDto) {
        return ResponseEntity.ok(sessionService.update(id, sessionDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteSession(@PathVariable String id) {
        sessionService.deleteById(id);
        return ResponseEntity.ok("Session deleted successfully");
    }
}