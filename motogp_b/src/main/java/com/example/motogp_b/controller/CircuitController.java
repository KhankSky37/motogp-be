package com.example.motogp_b.controller;

import com.example.motogp_b.dto.CircuitDto;
import com.example.motogp_b.service.CircuitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/circuits")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CircuitController {
    CircuitService circuitService;

    @GetMapping
    ResponseEntity<List<CircuitDto>> getCircuits() {
        return ResponseEntity.ok(circuitService.findAll());
    }

    @PostMapping
    ResponseEntity<CircuitDto> createCircuit(@RequestBody CircuitDto circuitDto) {
        return ResponseEntity.ok(circuitService.create(circuitDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<CircuitDto> getCircuit(@PathVariable String id) {
        return ResponseEntity.ok(circuitService.findById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<CircuitDto> updateCircuit(@PathVariable String id, @RequestBody CircuitDto circuitDto) {
        return ResponseEntity.ok(circuitService.update(id, circuitDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCircuit(@PathVariable String id) {
        circuitService.deleteById(id);
        return ResponseEntity.ok("Circuit deleted successfully");
    }
}