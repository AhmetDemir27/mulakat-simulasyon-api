package com.simulasyon.mulakat_simulasyon_api.dto;

import com.simulasyon.mulakat_simulasyon_api.entity.Durum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MulakatSessionDto {
private Long Id;
private UserDto user;
private String technology;
private String difficulty;
private Durum durum;
private LocalDateTime startTime;
private LocalDateTime finishTime;
}
