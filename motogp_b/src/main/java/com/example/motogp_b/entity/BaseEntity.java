package com.example.motogp_b.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public class BaseEntity {
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
