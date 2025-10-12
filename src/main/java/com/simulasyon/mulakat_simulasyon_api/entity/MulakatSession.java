package com.simulasyon.mulakat_simulasyon_api.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name ="mulakat_sessions")
public class MulakatSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "id" ,nullable = false)
    private User user;

    @Column(nullable = false)
    private String technology;

    @Column(nullable = false)
    private String difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Durum durum;

    @Column(name="start_time",nullable = false,updatable = false)
    private LocalDateTime startTime;

    @Column(name="finish_time")
    private LocalDateTime finishTime;

    @PrePersist
    protected void onCreate() {
        startTime = LocalDateTime.now();
        durum = Durum.BASLADI;
    }

}
