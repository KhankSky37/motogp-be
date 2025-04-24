package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.TeamDto;
import com.example.motogp_b.entity.Team;
import com.example.motogp_b.repository.TeamRepository;
import com.example.motogp_b.service.TeamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeamServiceImpl implements TeamService {
    TeamRepository teamRepository;
    ModelMapper modelMapper;
    
    @Override
    public List<TeamDto> findAll() {
        return teamRepository.findAll().stream()
                .map(team -> modelMapper.map(team, TeamDto.class))
                .toList();
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
    public TeamDto update(String id, TeamDto teamDto) {
        Team team = modelMapper.map(teamDto, Team.class);
        return modelMapper.map(teamRepository.save(team), TeamDto.class);
    }

    @Override
    public void deleteById(String id) {
        teamRepository.deleteById(id);
    }
}