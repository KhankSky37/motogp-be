package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.RiderStandingDto;
import com.example.motogp_b.dto.TeamStandingDto;
import com.example.motogp_b.repository.ResultRepository;
import com.example.motogp_b.service.StandingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StandingServiceImpl implements StandingService {
    ResultRepository resultRepository;
    @Override
    public List<RiderStandingDto> getRiderStandings(Integer season, String categoryId) {
        return resultRepository.getRiderStanding(season, categoryId);
    }

    @Override
    public List<TeamStandingDto> getTeamStandings(Integer season, String categoryId) {
        return resultRepository.getTeamStanding(season, categoryId);
    }

    @Override
    public List<RiderStandingDto> getRiderStandingsByBMW(Integer season) {
        return resultRepository.getRiderStandingByBMW(season);
    }
}
