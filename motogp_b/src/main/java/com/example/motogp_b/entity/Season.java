package com.example.motogp_b.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "season")
public class Season {
    @Id
    @Column(name = "season_id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @CreatedDate
    @Column(name = "created_date")
    LocalDateTime createdDate;

    @CreatedBy
    @Column(name = "created_user")
    String createUser;

    @LastModifiedDate
    @Column(name = "modified_date")
    Date modifiedDate;

    @LastModifiedBy
    @Column(name = "modified_user")
    String modifiedUser;
}