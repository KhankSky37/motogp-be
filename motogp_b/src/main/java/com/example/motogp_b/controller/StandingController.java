package com.example.motogp_b.controller;

import com.example.motogp_b.service.StandingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/standings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StandingController {
    StandingService standingService;

    @RequestMapping
    public ResponseEntity<?> getRiderStandings(String seasonYear, String categoryId, String type) {
        if ("rider".equals(type)) {
            return ResponseEntity.ok(standingService.getRiderStandings(seasonYear, categoryId));
        } else if ("team".equals(type)) {
            return ResponseEntity.ok(standingService.getTeamStandings(seasonYear, categoryId));
        } else return null;
    }
}
