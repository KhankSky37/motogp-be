package com.example.motogp_b.controller;

import com.example.motogp_b.dto.ManufacturerDto;
import com.example.motogp_b.service.ManufacturerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manufacturers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ManufacturerController {
    ManufacturerService manufacturerService;

    @GetMapping
    ResponseEntity<List<ManufacturerDto>> getManufacturers() {
        return ResponseEntity.ok(manufacturerService.findAll());
    }

    @PostMapping
    ResponseEntity<ManufacturerDto> createManufacturer(@RequestBody ManufacturerDto manufacturerDto) {
        return ResponseEntity.ok(manufacturerService.create(manufacturerDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<ManufacturerDto> getManufacturer(@PathVariable String id) {
        return ResponseEntity.ok(manufacturerService.findById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<ManufacturerDto> updateManufacturer(@PathVariable String id,
            @RequestBody ManufacturerDto manufacturerDto) {
        return ResponseEntity.ok(manufacturerService.update(id, manufacturerDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteManufacturer(@PathVariable String id) {
        manufacturerService.deleteById(id);
        return ResponseEntity.ok("Manufacturer deleted successfully");
    }
}