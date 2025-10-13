package com.simulasyon.mulakat_simulasyon_api.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendAnswerRequest {
    private Long questionId;
    private String answerText;
}
