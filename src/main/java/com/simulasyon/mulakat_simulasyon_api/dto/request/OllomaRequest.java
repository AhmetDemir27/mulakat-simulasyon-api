package com.simulasyon.mulakat_simulasyon_api.dto.request;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OllomaRequest {
    private String model;

    private String prompt;

    private Boolean stream;
}
