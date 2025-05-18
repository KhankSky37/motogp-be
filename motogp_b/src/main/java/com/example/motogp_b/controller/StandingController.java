package com.example.motogp_b.controller;

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

    @RequestMapping("/riders")
    public ResponseEntity<?> getRiderStandings() {

        return ResponseEntity.ok("Rider Standings");
    }

    @RequestMapping("/teams")
    public ResponseEntity<?> getTeamStandings() {

        return ResponseEntity.ok("Rider Standings");
    }

    @RequestMapping("/constructors")
    public ResponseEntity<?> getConstructorStandings() {

        return ResponseEntity.ok("Rider Standings");
    }

    @RequestMapping("/bmw")
    public ResponseEntity<?> getBMWStandings() {

        return ResponseEntity.ok("Rider Standings");
    }

}
