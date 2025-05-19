package com.example.motogp_b.service;

import com.example.motogp_b.dto.ConstructorStandingDto;
import com.example.motogp_b.dto.RiderStandingDto;
import com.example.motogp_b.dto.TeamStandingDto;

import java.util.List;

public interface StandingService {
    List<RiderStandingDto> getRiderStandings(Integer season, String categoryId);
    List<TeamStandingDto> getTeamStandings(Integer season, String categoryId);
    List<RiderStandingDto> getRiderStandingsByBMW(Integer season);
    List<ConstructorStandingDto> getConstructorStandings(Integer season, String categoryId);
}
