package com.simulasyon.mulakat_simulasyon_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "mulakat_sension_id",nullable = false)
    private MulakatSession mulakatSession;

    @Column(name ="question_text",columnDefinition = "TEXT",nullable = false)
    private String questionText;

    @Column(name="question_time",nullable = false,updatable = false)
    private LocalDateTime questionTime;

    @PrePersist
    protected void onCreate() {
        questionTime = LocalDateTime.now();
    }

}
