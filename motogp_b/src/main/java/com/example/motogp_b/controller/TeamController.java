package com.example.motogp_b.controller;

import com.example.motogp_b.dto.TeamDto;
import com.example.motogp_b.service.TeamService;
import com.example.motogp_b.service.FileStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeamController {
    TeamService teamService;
    FileStorageService fileStorageService;

    @GetMapping
    ResponseEntity<List<TeamDto>> getTeams(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String manufacturerId) {

        return ResponseEntity.ok(teamService.findAll(name, manufacturerId));
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    ResponseEntity<TeamDto> createTeam(
            @RequestPart("team") TeamDto teamDto,
            @RequestPart(value = "logo", required = false) MultipartFile logoFile) {
        return ResponseEntity.ok(teamService.create(teamDto, logoFile));
    }

    @GetMapping("/{id}")
    ResponseEntity<TeamDto> getTeam(@PathVariable String id) {
        return ResponseEntity.ok(teamService.findById(id));
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    ResponseEntity<TeamDto> updateTeam(
            @PathVariable String id,
            @RequestPart("team") TeamDto teamDto,
            @RequestPart(value = "logo", required = false) MultipartFile logoFile) {
        return ResponseEntity.ok(teamService.update(id, teamDto, logoFile));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTeam(@PathVariable String id) {
        teamService.deleteById(id);
        return ResponseEntity.ok("Team deleted successfully");
    }
}