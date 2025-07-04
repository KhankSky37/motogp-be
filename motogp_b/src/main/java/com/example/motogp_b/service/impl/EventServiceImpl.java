package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.EventDto;
import com.example.motogp_b.entity.Event;
import com.example.motogp_b.entity.Session;
import com.example.motogp_b.entity.Circuit;
import com.example.motogp_b.entity.Season;
import com.example.motogp_b.repository.EventRepository;
import com.example.motogp_b.repository.SessionRepository;
import com.example.motogp_b.repository.CircuitRepository;
import com.example.motogp_b.repository.SeasonRepository;
import com.example.motogp_b.service.EventService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventServiceImpl implements EventService {
    EventRepository eventRepository;
    SessionRepository sessionRepository;
    CircuitRepository circuitRepository;
    SeasonRepository seasonRepository;
    ModelMapper modelMapper;

    @Override
    public List<EventDto> findAll() {
        return eventRepository.findAll().stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .toList();
    }

    @Override
    public List<EventDto> findAllWithFilters(String keyword, String seasonId, String circuitId,
            String eventType, String startDateFrom, String startDateTo) {
        // Convert string dates to LocalDate if they're not null
        LocalDate fromDate = startDateFrom != null && !startDateFrom.isEmpty() ? LocalDate.parse(startDateFrom) : null;
        LocalDate toDate = startDateTo != null && !startDateTo.isEmpty() ? LocalDate.parse(startDateTo) : null;

        // Convert seasonId to Integer if it's not null
        Integer seasonIdInt = seasonId != null && !seasonId.isEmpty() ? Integer.parseInt(seasonId) : null;

        List<Event> events = eventRepository.findAllWithFilters(
                keyword, seasonIdInt, circuitId, eventType, fromDate, toDate);

        return events.stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .toList();
    }

    @Override
    public List<EventDto> getEventSearchOptions(Integer seasonId, String eventType) {
        List<Event> events = eventRepository.findAllWithFilters(null, seasonId, null, eventType, null, null);

        if (events == null || events.isEmpty()) {
            return Collections.emptyList();
        }

        return events.stream().map(event -> {
            EventDto eventDto = modelMapper.map(event, EventDto.class);

            Set<Session> sessionsForEvent = event.getSessions();
            if (sessionsForEvent != null && !sessionsForEvent.isEmpty()) {
                Set<String> categoryIds = sessionsForEvent.stream()
                        .filter(session -> session.getCategory() != null
                                && session.getCategory().getCategoryId() != null)
                        .map(session -> session.getCategory().getCategoryId())
                        .collect(Collectors.toSet());

                Set<String> sessionTypes = sessionsForEvent.stream()
                        .map(Session::getSessionType)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

                eventDto.setCategoryIds(categoryIds);
                eventDto.setSessionTypes(sessionTypes);
            } else {
                eventDto.setCategoryIds(Collections.emptySet());
                eventDto.setSessionTypes(Collections.emptySet());
            }
            return eventDto;
        }).collect(Collectors.toList());
    }

    @Override
    public EventDto findById(String id) {
        return modelMapper.map(eventRepository.findById(id), EventDto.class);
    }

    @Override
    public EventDto create(EventDto eventDto) {
        Event event = modelMapper.map(eventDto, Event.class);
        return modelMapper.map(eventRepository.save(event), EventDto.class);
    }

    @Override
    public EventDto update(String id, EventDto eventDto) {
        // Kiểm tra xem bản ghi cần cập nhật có tồn tại không
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));

        // Cập nhật các trường cơ bản từ DTO
        existingEvent.setName(eventDto.getName());
        existingEvent.setOfficialName(eventDto.getOfficialName());
        existingEvent.setStartDate(eventDto.getStartDate());
        existingEvent.setEndDate(eventDto.getEndDate());
        existingEvent.setEventType(eventDto.getEventType());

        // Xử lý liên kết Circuit
        if (eventDto.getCircuit() != null && eventDto.getCircuit().getId() != null) {
            // Kiểm tra nếu circuit cần thay đổi
            if (existingEvent.getCircuit() == null ||
                    !existingEvent.getCircuit().getId().equals(eventDto.getCircuit().getId())) {
                Circuit circuit = circuitRepository.findById(eventDto.getCircuit().getId())
                        .orElseThrow(() -> new RuntimeException(
                                "Circuit not found with ID: " + eventDto.getCircuit().getId()));
                existingEvent.setCircuit(circuit);
            }
        } else if (eventDto.getCircuit() == null) {
            existingEvent.setCircuit(null);
        }

        // Xử lý liên kết Season
        if (eventDto.getSeason() != null && eventDto.getSeason().getId() != null) {
            // Kiểm tra nếu season cần thay đổi
            if (existingEvent.getSeason() == null ||
                    !existingEvent.getSeason().getId().equals(eventDto.getSeason().getId())) {
                Season season = seasonRepository.findById(eventDto.getSeason().getId())
                        .orElseThrow(() -> new RuntimeException(
                                "Season not found with ID: " + eventDto.getSeason().getId()));
                existingEvent.setSeason(season);
            }
        } else if (eventDto.getSeason() == null) {
            existingEvent.setSeason(null);
        }

        // Đảm bảo ID không bị thay đổi
        existingEvent.setId(id);

        // Lưu entity đã cập nhật và trả về DTO
        Event updatedEvent = eventRepository.save(existingEvent);
        return modelMapper.map(updatedEvent, EventDto.class);
    }

    @Override
    public void deleteById(String id) {
        List<Session> sessions = sessionRepository.findByEventId(id);
        for (Session session : sessions) {
            session.setEvent(null); // Gỡ quan hệ event
            sessionRepository.save(session);
        }
        eventRepository.deleteById(id);
    }
}