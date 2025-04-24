package com.example.motogp_b.controller;

import com.example.motogp_b.dto.TeamDto;
import com.example.motogp_b.service.TeamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeamController {
    TeamService teamService;

    @GetMapping
    ResponseEntity<List<TeamDto>> getTeams() {
        return ResponseEntity.ok(teamService.findAll());
    }

    @PostMapping
    ResponseEntity<TeamDto> createTeam(@RequestBody TeamDto teamDto) {
        return ResponseEntity.ok(teamService.create(teamDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<TeamDto> getTeam(@PathVariable String id) {
        return ResponseEntity.ok(teamService.findById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<TeamDto> updateTeam(@PathVariable String id, @RequestBody TeamDto teamDto) {
        return ResponseEntity.ok(teamService.update(id, teamDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTeam(@PathVariable String id) {
        teamService.deleteById(id);
        return ResponseEntity.ok("Team deleted successfully");
    }
}