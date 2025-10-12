package com.simulasyon.mulakat_simulasyon_api.controller;


import com.simulasyon.mulakat_simulasyon_api.dto.request.MulakatStartRequest;
import com.simulasyon.mulakat_simulasyon_api.entity.MulakatSession;
import com.simulasyon.mulakat_simulasyon_api.service.MulakatSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mulakatlar/")
public class MulakatSessionController {

    private final MulakatSessionService mulakatSessionService;

    public MulakatSessionController(MulakatSessionService mulakatSessionService) {
        this.mulakatSessionService = mulakatSessionService;
    }

    @PostMapping
    public ResponseEntity<MulakatSession> startMulakat(@RequestBody MulakatStartRequest mulakatStartRequest) {
        Long userId = mulakatStartRequest.getUserId();
        String technology = mulakatStartRequest.getTechnology();

        MulakatSession newMulakat = mulakatSessionService.mulakatStart(userId,technology);

        return new ResponseEntity<>(newMulakat,HttpStatus.CREATED);
    }
}
