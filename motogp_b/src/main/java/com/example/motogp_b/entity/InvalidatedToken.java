package com.example.motogp_b.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "invalidated_token", schema = "it_recruitment")
public class InvalidatedToken {
    @Id
    @Column(name = "id", nullable = false)
    String id;

    @Column(name = "expiry_time")
    Date expiryTime;
}
