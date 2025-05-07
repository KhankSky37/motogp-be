package com.example.motogp_b.controller;

import com.example.motogp_b.dto.CircuitDto;
import com.example.motogp_b.service.CircuitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/circuits")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CircuitController {
    CircuitService circuitService;

    @GetMapping
    ResponseEntity<List<CircuitDto>> getCircuits(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String country) {

        CircuitDto circuitDto = new CircuitDto();
        circuitDto.setKeyword(keyword);

        // Map country to locationCountry if provided
        if (country != null && !country.isEmpty()) {
            circuitDto.setLocationCountry(country);
        }

        return ResponseEntity.ok(circuitService.findAll(circuitDto));
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    ResponseEntity<CircuitDto> createCircuit(
            @RequestPart("circuit") CircuitDto circuitDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        return ResponseEntity.ok(circuitService.create(circuitDto, imageFile));
    }

    @GetMapping("/{id}")
    ResponseEntity<CircuitDto> getCircuit(@PathVariable String id) {
        return ResponseEntity.ok(circuitService.findById(id));
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    ResponseEntity<CircuitDto> updateCircuit(
            @PathVariable String id,
            @RequestPart("circuit") CircuitDto circuitDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        return ResponseEntity.ok(circuitService.update(id, circuitDto, imageFile));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCircuit(@PathVariable String id) {
        circuitService.deleteById(id);
        return ResponseEntity.ok("Circuit deleted successfully");
    }
}