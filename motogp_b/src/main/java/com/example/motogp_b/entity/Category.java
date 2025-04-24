package com.example.motogp_b.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category extends BaseEntity{
    @Id
    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Column(name = "name")
    private String name;

}