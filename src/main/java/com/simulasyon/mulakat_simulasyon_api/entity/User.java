package com.simulasyon.mulakat_simulasyon_api.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String nameSurname;

    @Column(name = "profil_foto_url")
    private String profileFotoUrl;

    @Column(name="google_id",nullable = false,unique = true)
    private String googleId;

    @Column(name="olusturulma_zamani",nullable = false,updatable = false)
    private LocalDateTime olusturulmaZamani;
    @PrePersist
    protected void onCreate(){
        olusturulmaZamani= LocalDateTime.now();
    }

}
