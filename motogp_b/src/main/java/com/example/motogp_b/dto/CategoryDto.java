package com.example.motogp_b.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link com.example.motogp_b.entity.Category}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDto{
    LocalDateTime createdDate;
    String createUser;
    Date modifiedDate;
    String modifiedUser;
    String categoryId;
    String name;
}