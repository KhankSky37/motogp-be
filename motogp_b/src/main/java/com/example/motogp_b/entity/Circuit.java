package com.example.motogp_b.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "circuit")
public class Circuit extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "location_city")
    private String locationCity;

    @Column(name = "location_country")
    private String locationCountry;

    @Column(name = "length_km")
    private Integer lengthKm;

    @Column(name = "image_url")
    private String imageUrl;

}