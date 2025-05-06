package com.example.motogp_b.dto;

import com.example.motogp_b.entity.Circuit;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link Circuit}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CircuitDto {
    LocalDateTime createdDate;
    String createUser;
    Date modifiedDate;
    String modifiedUser;
    String id;
    String name;
    String locationCity;
    String locationCountry;
    Integer lengthKm;
    String imageUrl;

    // For search functionality
    String keyword;
}