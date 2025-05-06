package com.example.motogp_b.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link com.example.motogp_b.entity.Manufacturer}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManufacturerDto {
    LocalDateTime createdDate;
    String createUser;
    Date modifiedDate;
    String modifiedUser;
    String id;
    String name;
    String locationCountry;

    // For search functionality
    String keyword;
}