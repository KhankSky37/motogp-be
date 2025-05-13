package com.example.motogp_b.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventSummaryDto {
    String id;
    String name;
    String officialName;
    LocalDate startDate;
    LocalDate endDate;
    String eventType;
}
