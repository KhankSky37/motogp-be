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
import com.example.motogp_b.entity.Manufacturer;
import com.example.motogp_b.repository.ManufacturerRepository;

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
    ManufacturerRepository manufacturerRepository; // Added repository

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
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + id));

        // Update basic properties from DTO
        existingTeam.setName(teamDto.getName());
        // Note: Logo URL is handled in the overloaded update method with MultipartFile

        // Handle Manufacturer association
        if (teamDto.getManufacturer() != null && teamDto.getManufacturer().getId() != null) {
            // Check if the manufacturer needs to be changed
            if (existingTeam.getManufacturer() == null ||
                    !existingTeam.getManufacturer().getId().equals(teamDto.getManufacturer().getId())) {
                Manufacturer manufacturer = manufacturerRepository.findById(teamDto.getManufacturer().getId())
                        .orElseThrow(() -> new RuntimeException(
                                "Manufacturer not found with ID: " + teamDto.getManufacturer().getId()));
                existingTeam.setManufacturer(manufacturer);
            }
        } else if (teamDto.getManufacturer() == null) {
            // If DTO has no manufacturer, or its ID is null, disassociate the manufacturer
            existingTeam.setManufacturer(null);
        }
        // If teamDto.getManufacturer() is not null but its ID is null, it's ambiguous.
        // Current logic: if ID is null, it's treated as if no valid manufacturer is
        // provided for update.
        // If the intention is to create a new manufacturer, that should be a separate
        // process.

        Team updatedTeam = teamRepository.save(existingTeam);
        return modelMapper.map(updatedTeam, TeamDto.class);
    }

    @Override
    @Transactional
    public TeamDto update(String id, TeamDto teamDto, MultipartFile logoFile) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with ID: " + id));

        String oldLogoUrl = existingTeam.getLogoUrl();

        // Update basic properties (excluding ID and associations that are handled
        // manually)
        existingTeam.setName(teamDto.getName());
        // other scalar properties from teamDto can be set here if needed.
        // modelMapper.map(teamDto, existingTeam) could be used if configured carefully
        // to avoid altering IDs or mismanaging associations.

        // Handle Manufacturer association
        if (teamDto.getManufacturer() != null && teamDto.getManufacturer().getId() != null) {
            // Check if the manufacturer needs to be changed
            if (existingTeam.getManufacturer() == null ||
                    !existingTeam.getManufacturer().getId().equals(teamDto.getManufacturer().getId())) {
                Manufacturer manufacturer = manufacturerRepository.findById(teamDto.getManufacturer().getId())
                        .orElseThrow(() -> new RuntimeException(
                                "Manufacturer not found with ID: " + teamDto.getManufacturer().getId()));
                existingTeam.setManufacturer(manufacturer);
            }
        } else if (teamDto.getManufacturer() == null) {
            // If DTO has no manufacturer, or its ID is null, disassociate the manufacturer
            existingTeam.setManufacturer(null);
        }
        // Ensure ID is preserved (though findById and then save on the same object
        // should preserve it)
        // existingTeam.setId(id); // Usually not needed if you fetched the entity and
        // are updating it.

        // Handle logo file
        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                if (StringUtils.hasText(oldLogoUrl)) {
                    try {
                        fileStorageService.deleteFile(oldLogoUrl);
                    } catch (IOException e) {
                        System.err
                                .println("Could not delete old logo file: " + oldLogoUrl + " Error: " + e.getMessage());
                    }
                }
                String newLogoUrl = fileStorageService.storeFile(logoFile, TEAM_SUBDIRECTORY);
                existingTeam.setLogoUrl(newLogoUrl);
            } catch (IOException e) {
                throw new RuntimeException("Could not store logo file for team: " + id, e);
            }
        } else {
            // If no new logo file is provided, keep the old one.
            // This line is only needed if modelMapper.map(teamDto, existingTeam) was used
            // and might have nulled it.
            // existingTeam.setLogoUrl(oldLogoUrl);
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