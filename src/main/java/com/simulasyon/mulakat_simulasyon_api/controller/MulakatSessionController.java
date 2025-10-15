package com.simulasyon.mulakat_simulasyon_api.controller;


import com.simulasyon.mulakat_simulasyon_api.dto.MulakatSessionDto;
import com.simulasyon.mulakat_simulasyon_api.dto.request.MulakatStartRequest;
import com.simulasyon.mulakat_simulasyon_api.dto.request.SendAnswerRequest;
import com.simulasyon.mulakat_simulasyon_api.dto.response.AnswerEvaluationResponse;
import com.simulasyon.mulakat_simulasyon_api.dto.response.MulakatStartResponse;
import com.simulasyon.mulakat_simulasyon_api.service.MulakatSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mulakatlar/")
public class MulakatSessionController {

    private final MulakatSessionService mulakatSessionService;

    public MulakatSessionController(MulakatSessionService mulakatSessionService) {
        this.mulakatSessionService = mulakatSessionService;
    }

    @PostMapping
    public ResponseEntity<MulakatStartResponse> startMulakat(@RequestBody MulakatStartRequest mulakatStartRequest) {
        Long userId = mulakatStartRequest.getUserId();
        String technology = mulakatStartRequest.getTechnology();
        String difficulty = mulakatStartRequest.getDifficulty();
        Integer totalCountOfQuestion = mulakatStartRequest.getTotalCountOfQuestion();

        MulakatStartResponse response = mulakatSessionService.mulakatStart(userId,technology,difficulty,totalCountOfQuestion);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping("/{sessionId}/answer")
    public ResponseEntity<AnswerEvaluationResponse> sendAnswer(@PathVariable Long sessionId,
                                                               @RequestBody SendAnswerRequest sendAnswerRequest) {
        AnswerEvaluationResponse response = mulakatSessionService.answerEvaluation(
                sessionId,
                sendAnswerRequest.getQuestionId(),
                sendAnswerRequest.getAnswerText());
    return ResponseEntity.ok(response);
    }
    @PostMapping("/{sessionId}/finish/")
    public ResponseEntity<MulakatSessionDto> finishMulakat(@PathVariable Long sessionId){

         MulakatSessionDto mulakatfinishDto =  mulakatSessionService.mulakatFinish(sessionId);

         return ResponseEntity.ok(mulakatfinishDto);

    }

    @GetMapping
    public ResponseEntity<List<MulakatSessionDto>> getMulakatlarByUser(
            @RequestParam(name = "userId") Long userId){
        List<MulakatSessionDto> mulakatlar = mulakatSessionService.kullanicininMulakatlariniListele(userId);
        return ResponseEntity.ok(mulakatlar);
    }
}
