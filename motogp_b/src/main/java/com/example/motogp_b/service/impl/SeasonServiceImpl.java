package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.SeasonDto;
import com.example.motogp_b.entity.Season;
import com.example.motogp_b.repository.SeasonRepository;
import com.example.motogp_b.service.SeasonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeasonServiceImpl implements SeasonService {
    SeasonRepository seasonRepository;
    ModelMapper modelMapper;
    @Override
    public List<SeasonDto> findAll() {
        return seasonRepository.findAll().stream()
                .map(season -> modelMapper.map(season, SeasonDto.class))
                .toList();
    }

    @Override
    public SeasonDto findById(Integer id) {
        return modelMapper.map(seasonRepository.findById(id), SeasonDto.class);
    }

    @Override
    public SeasonDto create(SeasonDto seasonDto) {
        Season season = modelMapper.map(seasonDto, Season.class);
        return modelMapper.map(seasonRepository.save(season), SeasonDto.class);
    }

    @Override
    public SeasonDto update(Integer id, SeasonDto seasonDto) {
        Season season = modelMapper.map(seasonDto, Season.class);
        return modelMapper.map(seasonRepository.save(season), SeasonDto.class);
    }

    @Override
    public void deleteById(Integer id) {
        seasonRepository.deleteById(id);
    }
}
