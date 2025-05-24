package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.ResultDto;
import com.example.motogp_b.entity.Result;
import com.example.motogp_b.entity.Session;
import com.example.motogp_b.entity.Rider;
import com.example.motogp_b.entity.Team;
import com.example.motogp_b.entity.Manufacturer;
import com.example.motogp_b.repository.ResultRepository;
import com.example.motogp_b.repository.SessionRepository;
import com.example.motogp_b.repository.RiderRepository;
import com.example.motogp_b.repository.TeamRepository;
import com.example.motogp_b.repository.ManufacturerRepository;
import com.example.motogp_b.service.ResultService;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResultServiceImpl implements ResultService {
    ResultRepository resultRepository;
    SessionRepository sessionRepository;
    RiderRepository riderRepository;
    TeamRepository teamRepository;
    ManufacturerRepository manufacturerRepository;
    ModelMapper modelMapper;

    private static final List<Integer> RACE_POINTS_DISTRIBUTION = Arrays.asList(
            25, 20, 16, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);

    private static final List<Integer> SPRINT_RACE_POINTS_DISTRIBUTION = Arrays.asList(
            12, 9, 7, 6, 5, 4, 3, 2, 1);

    @Override
    public List<ResultDto> findAll() {
        return resultRepository.findAll().stream()
                .map(result -> modelMapper.map(result, ResultDto.class))
                .toList();
    }

    @Override
    public List<ResultDto> findAll(String sessionId, String riderId, String teamId, String manufacturerId,
            Integer position,
            String status) {
        return resultRepository.search(sessionId, riderId, teamId, manufacturerId, position, status).stream()
                .map(resultEntity -> {
                    ResultDto resultDto = modelMapper.map(resultEntity, ResultDto.class);
                    if (resultEntity.getSession() != null) {
                        resultDto.setSessionId(resultEntity.getSession().getId());
                    }
                    return resultDto;
                })
                .toList();
    }

    @Override
    public ResultDto findById(String id) {
        return resultRepository.findById(id)
                .map(result -> {
                    ResultDto resultDto = modelMapper.map(result, ResultDto.class);
                    if (result.getSession() != null) {
                        resultDto.setSessionId(result.getSession().getId());
                    }
                    return resultDto;
                })
                .orElse(null);
    }

    @Override
    public ResultDto create(ResultDto resultDto) {
        Result result = modelMapper.map(resultDto, Result.class);

        if (resultDto.getSessionId() != null) {
            Session session = sessionRepository.findById(resultDto.getSessionId())
                    .orElseThrow(() -> new RuntimeException("Session not found with id: " + resultDto.getSessionId()));
            result.setSession(session);

            String sessionTypeLower = session.getSessionType().toLowerCase();
            Integer position = result.getPosition();

            if (position != null && position > 0) {
                int points = 0;
                if ("rac1".equalsIgnoreCase(sessionTypeLower) || "rac2".equalsIgnoreCase(sessionTypeLower)) {
                    points = getPointsFromDistribution(position, RACE_POINTS_DISTRIBUTION);
                } else if ("spr".equalsIgnoreCase(sessionTypeLower)) {
                    points = getPointsFromDistribution(position, SPRINT_RACE_POINTS_DISTRIBUTION);
                } else if (sessionTypeLower.startsWith("q")) {
                    points = getPointsFromDistribution(position, RACE_POINTS_DISTRIBUTION);
                }
                result.setPoints(points);
            } else {
                result.setPoints(0);
            }
        } else {
            result.setPoints(0);
        }

        Result savedResult = resultRepository.save(result);
        return modelMapper.map(savedResult, ResultDto.class);
    }

    private int getPointsFromDistribution(int position, List<Integer> pointsDistribution) {
        if (position > 0 && position <= pointsDistribution.size()) {
            return pointsDistribution.get(position - 1);
        }
        return 0;
    }

    @Override
    public ResultDto update(String id, ResultDto resultDto) {
        Result existingResult = resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found with id: " + id));

        // Cập nhật các trường cơ bản từ DTO
        existingResult.setPosition(resultDto.getPosition());
        existingResult.setTimeMillis(resultDto.getTimeMillis());
        existingResult.setGapMillis(resultDto.getGapMillis());
        existingResult.setLaps(resultDto.getLaps());
        existingResult.setStatus(resultDto.getStatus());
        // Không cập nhật createUser và createdDate ở đây, chúng nên được giữ nguyên

        // Xử lý Session
        if (resultDto.getSessionId() != null) {
            Session session = sessionRepository.findById(resultDto.getSessionId())
                    .orElseThrow(() -> new RuntimeException("Session not found with id: " + resultDto.getSessionId()));
            existingResult.setSession(session);
        } else {
            existingResult.setSession(null); // Cho phép xóa session khỏi result
        }

        // Xử lý Rider
        if (resultDto.getRiderId() != null) {
            Rider rider = riderRepository.findById(resultDto.getRiderId())
                    .orElseThrow(() -> new RuntimeException("Rider not found with id: " + resultDto.getRiderId()));
            existingResult.setRider(rider);
        } else {
            existingResult.setRider(null);
        }

        // Xử lý Team
        if (resultDto.getTeamId() != null) {
            Team team = teamRepository.findById(resultDto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id: " + resultDto.getTeamId()));
            existingResult.setTeam(team);
        } else {
            existingResult.setTeam(null);
        }

        // Xử lý Manufacturer
        if (resultDto.getManufacturerId() != null) {
            Manufacturer manufacturer = manufacturerRepository.findById(resultDto.getManufacturerId())
                    .orElseThrow(() -> new RuntimeException(
                            "Manufacturer not found with id: " + resultDto.getManufacturerId()));
            existingResult.setManufacturer(manufacturer);
        } else {
            existingResult.setManufacturer(null);
        }

        // Tính toán lại điểm dựa trên session và position mới (nếu có)
        if (existingResult.getSession() != null && existingResult.getPosition() != null
                && existingResult.getPosition() > 0) {
            String sessionTypeLower = existingResult.getSession().getSessionType().toLowerCase();
            Integer position = existingResult.getPosition();
            int points = 0;
            if ("rac1".equalsIgnoreCase(sessionTypeLower) || "rac2".equalsIgnoreCase(sessionTypeLower)) {
                points = getPointsFromDistribution(position, RACE_POINTS_DISTRIBUTION);
            } else if ("spr".equalsIgnoreCase(sessionTypeLower)) {
                points = getPointsFromDistribution(position, SPRINT_RACE_POINTS_DISTRIBUTION);
            } else if (sessionTypeLower.startsWith("q")) {
                points = getPointsFromDistribution(position, RACE_POINTS_DISTRIBUTION);
            }
            existingResult.setPoints(points);
        } else {
            existingResult.setPoints(0); // Nếu không có session hoặc position hợp lệ, điểm là 0
        }

        Result savedResult = resultRepository.save(existingResult);

        // Map lại sang DTO để trả về, đảm bảo các ID được phản ánh đúng
        ResultDto updatedDto = modelMapper.map(savedResult, ResultDto.class);
        if (savedResult.getSession() != null) {
            updatedDto.setSessionId(savedResult.getSession().getId());
        } else {
            updatedDto.setSessionId(null);
        }
        if (savedResult.getRider() != null) {
            updatedDto.setRiderId(savedResult.getRider().getRiderId());
        } else {
            updatedDto.setRiderId(null);
        }
        if (savedResult.getTeam() != null) {
            updatedDto.setTeamId(savedResult.getTeam().getId());
        } else {
            updatedDto.setTeamId(null);
        }
        if (savedResult.getManufacturer() != null) {
            updatedDto.setManufacturerId(savedResult.getManufacturer().getId());
        } else {
            updatedDto.setManufacturerId(null);
        }

        return updatedDto;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        try {
            // Lấy result cần xóa
            Result result = resultRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Result not found with ID: " + id));

            // Xóa result
            resultRepository.delete(result);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting result: " + e.getMessage(), e);
        }
    }
}