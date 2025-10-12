package com.simulasyon.mulakat_simulasyon_api.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OllomaResponse {
    private String model;
    private String response;
    @JsonProperty("created_at")
    private String createdAt;
    private Boolean done;
}
