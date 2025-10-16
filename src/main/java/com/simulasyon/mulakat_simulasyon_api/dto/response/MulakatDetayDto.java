package com.simulasyon.mulakat_simulasyon_api.dto.response;

import com.simulasyon.mulakat_simulasyon_api.dto.MulakatSessionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MulakatDetayDto {
    private MulakatSessionDto mulakatSessionDto;
    private List<SoruCevapPairDto> soruCevapPairDto;
    private Double GenelPaun;
}
