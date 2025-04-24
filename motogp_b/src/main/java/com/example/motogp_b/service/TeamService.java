package com.example.motogp_b.service;

import com.example.motogp_b.dto.TeamDto;

import java.util.List;

public interface TeamService {
    List<TeamDto> findAll();
    TeamDto findById(String id);
    TeamDto create(TeamDto teamDto);
    TeamDto update(String id, TeamDto teamDto);
    void deleteById(String id);
}