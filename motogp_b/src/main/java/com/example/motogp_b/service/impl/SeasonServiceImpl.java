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
    public List<SeasonDto> findAll(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return seasonRepository.findAll().stream()
                    .map(season -> modelMapper.map(season, SeasonDto.class))
                    .toList();
        } else {
            String searchTerm = "%" + keyword.toLowerCase() + "%";
            return seasonRepository.search(searchTerm).stream()
                    .map(season -> modelMapper.map(season, SeasonDto.class))
                    .toList();
        }
    }

    @Override
    public SeasonDto findById(Integer id) {
        return modelMapper.map(seasonRepository.findById(id), SeasonDto.class);
    }

    @Override
    public SeasonDto create(SeasonDto seasonDto) {
        // Kiểm tra xem ID đã tồn tại chưa
        if (seasonRepository.existsById(seasonDto.getId())) {
            throw new RuntimeException("ID mùa giải đã tồn tại. Vui lòng sử dụng ID khác.");
        }

        Season season = modelMapper.map(seasonDto, Season.class);
        return modelMapper.map(seasonRepository.save(season), SeasonDto.class);
    }

    @Override
    public SeasonDto update(Integer id, SeasonDto seasonDto) {
        // Kiểm tra nếu ID mới khác với ID cũ và đã tồn tại
        if (!id.equals(seasonDto.getId()) && seasonRepository.existsById(seasonDto.getId())) {
            throw new RuntimeException("ID mùa giải đã tồn tại. Vui lòng sử dụng ID khác.");
        }

        Season season = modelMapper.map(seasonDto, Season.class);
        return modelMapper.map(seasonRepository.save(season), SeasonDto.class);
    }

    @Override
    public void deleteById(Integer id) {
        seasonRepository.deleteById(id);
    }
}
