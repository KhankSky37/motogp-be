package com.example.motogp_b.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.validation.constraints.Pattern;

import jakarta.persistence.Id;

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
    @Id
    @Pattern(
        regexp = "^#[A-Za-z0-9]", 
        message = "categoryId phải bắt đầu bằng # và chỉ chứa chữ cái hoặc số."
    )
    String categoryId;
    String name;
}