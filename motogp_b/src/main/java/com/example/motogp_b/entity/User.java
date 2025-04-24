package com.example.motogp_b.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "surname")
    private String surname;

    @Column(name = "nickname")
    private String nickname;

    @Lob
    @Column(name = "gender")
    private String gender;

    @Column(name = "data_of_birth")
    private LocalDate dataOfBirth;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_favourite")
    private Rider riderFavourite;

    @Column(name = "role")
    private String role;

}