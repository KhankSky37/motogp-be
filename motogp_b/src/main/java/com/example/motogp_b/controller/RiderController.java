package com.example.motogp_b.controller;

import com.example.motogp_b.dto.RiderDto;
import com.example.motogp_b.service.RiderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RiderController {
    RiderService riderService;

    @GetMapping
    ResponseEntity<List<RiderDto>> getRiders() {
        return ResponseEntity.ok(riderService.findAll());
    }

    @PostMapping
    ResponseEntity<RiderDto> createRider(@RequestBody RiderDto riderDto) {
        return ResponseEntity.ok(riderService.create(riderDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<RiderDto> getRider(@PathVariable String id) {
        return ResponseEntity.ok(riderService.findById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<RiderDto> updateRider(@PathVariable String id, @RequestBody RiderDto riderDto) {
        return ResponseEntity.ok(riderService.update(id, riderDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteRider(@PathVariable String id) {
        riderService.deleteById(id);
        return ResponseEntity.ok("Rider deleted successfully");
    }
}