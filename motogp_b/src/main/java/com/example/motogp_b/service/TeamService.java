package com.example.motogp_b.service;

import com.example.motogp_b.dto.TeamDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeamService {
    List<TeamDto> findAll();

    List<TeamDto> findAll(String name, String manufacturerId);

    TeamDto findById(String id);

    TeamDto create(TeamDto teamDto);

    TeamDto create(TeamDto teamDto, MultipartFile logoFile);

    TeamDto update(String id, TeamDto teamDto);

    TeamDto update(String id, TeamDto teamDto, MultipartFile logoFile);

    void deleteById(String id);
}