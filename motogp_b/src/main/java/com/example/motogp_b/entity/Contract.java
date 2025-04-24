package com.example.motogp_b.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "contract")
public class Contract extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "rider_id")
    private String riderId;

    @Column(name = "season_id")
    private Integer seasonId;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "rider_role")
    private String riderRole;

}