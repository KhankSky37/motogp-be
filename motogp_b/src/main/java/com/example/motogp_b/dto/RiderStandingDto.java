package com.example.motogp_b.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RiderStandingDto {
    String firstName;
    String lastName;
    String nationality;
    String photoUrl;
    String teamName;
    Long points;
}
