package com.simulasyon.mulakat_simulasyon_api.dto.response;


import com.simulasyon.mulakat_simulasyon_api.entity.MulakatSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MulakatStartResponse {
    private MulakatSession sessionDetails;
    private String firstQuestionText;
}
