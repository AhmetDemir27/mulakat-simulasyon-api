package com.simulasyon.mulakat_simulasyon_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginRequest {
    private String googleId;
    private String email;
    private String nameSurname;
    private String profileFotoUrl;
}
