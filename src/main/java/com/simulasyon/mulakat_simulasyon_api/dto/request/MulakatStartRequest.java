package com.simulasyon.mulakat_simulasyon_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MulakatStartRequest {
    private Long userId;
    private String technology;
    private String difficulty;
}
