package com.example.motogp_b.dto;

import com.example.motogp_b.entity.Event;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * DTO for {@link com.example.motogp_b.entity.Session}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionDto{
    LocalDateTime createdDate;
    String createUser;
    Date modifiedDate;
    String modifiedUser;
    String id;
    CategoryDto category;
    String sessionType;
    Instant sessionDatetime;
    EventSummaryDto event;
    Set<ResultDto> results;
}