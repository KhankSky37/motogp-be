package com.example.motogp_b.controller;

import com.example.motogp_b.dto.SeasonDto;
import com.example.motogp_b.service.SeasonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seasons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeasonController {
    SeasonService seasonService;

    @GetMapping
    ResponseEntity<List<SeasonDto>> getSeasons(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(seasonService.findAll(keyword));
    }

    @PostMapping
    ResponseEntity<SeasonDto> createSeason(@RequestBody SeasonDto seasonDto) {
        return ResponseEntity.ok(seasonService.create(seasonDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<SeasonDto> getSeason(@PathVariable Integer id) {
        return ResponseEntity.ok(seasonService.findById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<SeasonDto> updateSeason(@PathVariable Integer id, @RequestBody SeasonDto seasonDto) {
        return ResponseEntity.ok(seasonService.update(id, seasonDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteSeason(@PathVariable Integer id) {
        seasonService.deleteById(id);
        return ResponseEntity.ok("Season deleted successfully");
    }

}
