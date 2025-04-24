package com.example.motogp_b.controller;

import com.example.motogp_b.dto.ResultDto;
import com.example.motogp_b.service.ResultService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResultController {
    ResultService resultService;

    @GetMapping
    ResponseEntity<List<ResultDto>> getResults() {
        return ResponseEntity.ok(resultService.findAll());
    }

    @PostMapping
    ResponseEntity<ResultDto> createResult(@RequestBody ResultDto resultDto) {
        return ResponseEntity.ok(resultService.create(resultDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<ResultDto> getResult(@PathVariable String id) {
        return ResponseEntity.ok(resultService.findById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<ResultDto> updateResult(@PathVariable String id, @RequestBody ResultDto resultDto) {
        return ResponseEntity.ok(resultService.update(id, resultDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteResult(@PathVariable String id) {
        resultService.deleteById(id);
        return ResponseEntity.ok("Result deleted successfully");
    }
}