package com.simulasyon.mulakat_simulasyon_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoruCevapPairDto {
    private String questionText;
    private String answerText;
    private String callback;
    private Integer puan;
}
