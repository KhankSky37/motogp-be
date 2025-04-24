package com.example.motogp_b.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link com.example.motogp_b.entity.Contract}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractDto{
    LocalDateTime createdDate;
    String createUser;
    Date modifiedDate;
    String modifiedUser;
    String id;
    String teamId;
    String riderId;
    Integer seasonId;
    String categoryId;
    String riderRole;
}