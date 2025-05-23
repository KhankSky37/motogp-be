package com.example.motogp_b.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "result")
public class Result extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "session_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "rider_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Rider rider;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "team_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Team team;

    @Column(name = "position")
    private Integer position;

    @Column(name = "time_millis")
    private Integer timeMillis;

    @Column(name = "gap_millis")
    private Integer gapMillis;

    @Column(name = "laps")
    private Integer laps;

    @Column(name = "points")
    private Integer points;
    @Column(name = "status")
    private String status;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "manufacturer_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Manufacturer manufacturer;

}