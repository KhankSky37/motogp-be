package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.TeamDto;
import com.example.motogp_b.entity.Team;
import com.example.motogp_b.repository.TeamRepository;
import com.example.motogp_b.service.FileStorageService;
import com.example.motogp_b.service.TeamService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import com.example.motogp_b.entity.Result;
import com.example.motogp_b.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeamServiceImpl implements TeamService {
    TeamRepository teamRepository;
    ResultRepository resultRepository;
    ModelMapper modelMapper;
    FileStorageService fileStorageService;

    private static final String TEAM_SUBDIRECTORY = "teams";

    @Override
    public List<TeamDto> findAll() {
        return teamRepository.findAll().stream()
                .map(team -> modelMapper.map(team, TeamDto.class))
                .toList();
    }

    @Override
    public List<TeamDto> findAll(String name, String manufacturerId) {
        // Get all teams first
        List<Team> teams = teamRepository.findAll();

        // Apply filters
        return teams.stream()
                .filter(team -> name == null || name.isEmpty() ||
                        team.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(team -> manufacturerId == null || manufacturerId.isEmpty() ||
                        (team.getManufacturer() != null && team.getManufacturer().getId().equals(manufacturerId)))
                .map(team -> modelMapper.map(team, TeamDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TeamDto findById(String id) {
        return modelMapper.map(teamRepository.findById(id), TeamDto.class);
    }

    @Override
    public TeamDto create(TeamDto teamDto) {
        Team team = modelMapper.map(teamDto, Team.class);
        return modelMapper.map(teamRepository.save(team), TeamDto.class);
    }

    @Override
    @Transactional
    public TeamDto create(TeamDto teamDto, MultipartFile logoFile) {
        Team team = modelMapper.map(teamDto, Team.class);

        // Handle logo file if provided
        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                String logoUrl = fileStorageService.storeFile(logoFile, TEAM_SUBDIRECTORY);
                team.setLogoUrl(logoUrl);
            } catch (IOException e) {
                throw new RuntimeException("Could not store logo file for team", e);
            }
        }

        Team savedTeam = teamRepository.save(team);
        return modelMapper.map(savedTeam, TeamDto.class);
    }

    @Override
    public TeamDto update(String id, TeamDto teamDto) {
        Team team = modelMapper.map(teamDto, Team.class);
        return modelMapper.map(teamRepository.save(team), TeamDto.class);
    }

    @Override
    @Transactional
    public TeamDto update(String id, TeamDto teamDto, MultipartFile logoFile) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + id));

        // Save the existing logo URL before mapping
        String oldLogoUrl = existingTeam.getLogoUrl();

        // Map DTO values to entity
        modelMapper.map(teamDto, existingTeam);

        // Ensure ID is preserved
        existingTeam.setId(id);

        // Handle logo file if provided
        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                // Delete old logo if exists
                if (StringUtils.hasText(oldLogoUrl)) {
                    try {
                        fileStorageService.deleteFile(oldLogoUrl);
                    } catch (IOException e) {
                        // Log error but continue with update
                        System.err.println("Could not delete old logo file: " + oldLogoUrl);
                    }
                }

                // Store new logo
                String newLogoUrl = fileStorageService.storeFile(logoFile, TEAM_SUBDIRECTORY);
                existingTeam.setLogoUrl(newLogoUrl);
            } catch (IOException e) {
                throw new RuntimeException("Could not store logo file for team: " + id, e);
            }
        } else {
            // No new logo provided, preserve the existing logo
            existingTeam.setLogoUrl(oldLogoUrl);
        }

        Team updatedTeam = teamRepository.save(existingTeam);
        return modelMapper.map(updatedTeam, TeamDto.class);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        // Get team to delete its logo if exists
        teamRepository.findById(id).ifPresent(team -> {
            // Handle associated results first
            List<Result> results = resultRepository.findByTeamId(id);
            for (Result result : results) {
                result.setTeam(null); // Remove the association
                resultRepository.save(result);
            }
           

            // Then try to delete the logo if it exists
            if (StringUtils.hasText(team.getLogoUrl())) {
                try {
                    fileStorageService.deleteFile(team.getLogoUrl());
                } catch (IOException e) {
                    // Log error but continue with deletion
                    System.err.println("Could not delete logo file: " + team.getLogoUrl());
                }
            }
        });

        teamRepository.deleteById(id);
    }
}