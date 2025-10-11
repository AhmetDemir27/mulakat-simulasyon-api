package com.simulasyon.mulakat_simulasyon_api.dto;

import com.simulasyon.mulakat_simulasyon_api.entity.MulakatSession;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MulakatStartRequest {
    private Long userId;
    private String technology;
}
