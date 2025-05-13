package com.example.motogp_b.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * DTO for {@link com.example.motogp_b.entity.Event}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDto{
    LocalDateTime createdDate;
    String createUser;
    Date modifiedDate;
    String modifiedUser;
    String id;
    SeasonDto season;
    CircuitDto circuit;
    String name;
    String officialName;
    LocalDate startDate;
    LocalDate endDate;
    String eventType;
    Set<SessionSummaryDto> sessions;
}