package com.simulasyon.mulakat_simulasyon_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name ="answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="question_id",referencedColumnName = "id", nullable = false)
    private Question question;

    @Column(name="answer_text",columnDefinition = "TEXT",nullable = false)
    private String answerText;

    @Column(name="callback",columnDefinition = "TEXT")
    private String callback;

    @Column(name = "puan")
    private Integer puan;

    @Column(name="answer_time")
    private LocalDateTime answerTime;


    @PrePersist
    public void onCreat() {
        this.answerTime = LocalDateTime.now();
    }
}
