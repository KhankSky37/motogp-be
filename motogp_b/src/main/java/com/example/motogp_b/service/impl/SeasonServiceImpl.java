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

import java.time.LocalDateTime;
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
        // Không cho phép thay đổi ID - luôn sử dụng ID từ đường dẫn
        if (!id.equals(seasonDto.getId())) {
            throw new RuntimeException("Không thể thay đổi ID mùa giải. ID phải giữ nguyên.");
        } // Kiểm tra mùa giải có tồn tại không và lấy dữ liệu hiện có
        Season existingSeason = seasonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mùa giải với ID: " + id));

        // Lưu lại các trường tạo mới để không bị mất
        LocalDateTime createdDate = existingSeason.getCreatedDate();
        String createUser = existingSeason.getCreateUser();

        // Map từ DTO mới vào entity
        Season updatedSeason = modelMapper.map(seasonDto, Season.class);

        // Giữ lại các trường tạo mới
        updatedSeason.setCreatedDate(createdDate);
        updatedSeason.setCreateUser(createUser);

        return modelMapper.map(seasonRepository.save(updatedSeason), SeasonDto.class);
    }

    @Override
    public void deleteById(Integer id) {
        seasonRepository.deleteById(id);
    }
}
