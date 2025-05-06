package com.example.motogp_b.service;

import com.example.motogp_b.dto.SessionDto;

import java.util.List;

public interface SessionService {
    List<SessionDto> findAll(String eventId, String categoryId, String sessionType, String dateFrom, String dateTo);

    SessionDto findById(String id);

    SessionDto create(SessionDto sessionDto);

    SessionDto update(String id, SessionDto sessionDto);

    void deleteById(String id);
}