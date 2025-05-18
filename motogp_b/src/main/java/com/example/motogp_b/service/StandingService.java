package com.example.motogp_b.service;

import com.example.motogp_b.dto.RiderStandingDto;
import com.example.motogp_b.dto.TeamStandingDto;

import java.util.List;

public interface StandingService {
    List<RiderStandingDto> getRiderStandings(String season, String categoryId);
    List<TeamStandingDto> getTeamStandings(String season, String categoryId);
}
