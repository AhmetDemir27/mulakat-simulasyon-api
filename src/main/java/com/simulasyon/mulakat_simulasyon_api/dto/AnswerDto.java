package com.simulasyon.mulakat_simulasyon_api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnswerDto {
    private Long id;
    private QuestionDto question;
    private String answerText;
    private String callback;
    private Integer puan;
    private LocalDateTime answerTime;
}
