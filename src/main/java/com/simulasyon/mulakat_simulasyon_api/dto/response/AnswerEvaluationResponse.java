package com.simulasyon.mulakat_simulasyon_api.dto.response;

import com.simulasyon.mulakat_simulasyon_api.dto.AnswerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerEvaluationResponse {
    private AnswerDto AnswerEvaluation;
    private Long nextQuestionId;
    private String nextQuestionText;
}
