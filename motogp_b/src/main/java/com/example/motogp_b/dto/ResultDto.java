package com.example.motogp_b.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link com.example.motogp_b.entity.Result}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultDto{
    LocalDateTime createdDate;
    String createUser;
    Date modifiedDate;
    String modifiedUser;
    String id;
    RiderDto rider;
    TeamDto team;
    Integer position;
    Integer timeMillis;
    Integer gapMillis;
    Integer laps;
    Integer points;
    String status;
    ManufacturerDto manufacturer;

    String sessionId;
}