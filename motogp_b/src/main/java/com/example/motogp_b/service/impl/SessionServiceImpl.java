package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.SessionDto;
import com.example.motogp_b.entity.Session;
import com.example.motogp_b.repository.SessionRepository;
import com.example.motogp_b.service.SessionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionServiceImpl implements SessionService {
    SessionRepository sessionRepository;
    ModelMapper modelMapper;

    @Override
    public List<SessionDto> findAll(String eventId, String categoryId, String sessionType, String dateFrom,
            String dateTo) {
        List<Session> sessions;

        if (eventId == null && categoryId == null && sessionType == null && dateFrom == null && dateTo == null) {
            // No filters, return all
            sessions = sessionRepository.findAll();
        } else {
            // Convert string dates to Instant if provided
            Instant fromDate = dateFrom != null && !dateFrom.isEmpty() ? Instant.parse(dateFrom) : null;
            Instant toDate = dateTo != null && !dateTo.isEmpty() ? Instant.parse(dateTo) : null;

            sessions = sessionRepository.search(eventId, categoryId, sessionType, fromDate, toDate);
        }

        return sessions.stream()
                .map(session -> modelMapper.map(session, SessionDto.class))
                .toList();
    }

    @Override
    public SessionDto findById(String id) {
        return modelMapper.map(sessionRepository.findById(id), SessionDto.class);
    }

    @Override
    public SessionDto create(SessionDto sessionDto) {
        Session session = modelMapper.map(sessionDto, Session.class);
        return modelMapper.map(sessionRepository.save(session), SessionDto.class);
    }

    @Override
    public SessionDto update(String id, SessionDto sessionDto) {
        Session session = modelMapper.map(sessionDto, Session.class);
        return modelMapper.map(sessionRepository.save(session), SessionDto.class);
    }

    @Override
    public void deleteById(String id) {
        sessionRepository.deleteById(id);
    }
}