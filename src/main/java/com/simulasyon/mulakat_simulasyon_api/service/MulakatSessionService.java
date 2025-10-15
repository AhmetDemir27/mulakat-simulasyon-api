package com.simulasyon.mulakat_simulasyon_api.service;

import com.simulasyon.mulakat_simulasyon_api.dto.AnswerDto;
import com.simulasyon.mulakat_simulasyon_api.dto.MulakatSessionDto;
import com.simulasyon.mulakat_simulasyon_api.dto.QuestionDto;
import com.simulasyon.mulakat_simulasyon_api.dto.UserDto;
import com.simulasyon.mulakat_simulasyon_api.dto.response.AnswerEvaluationResponse;
import com.simulasyon.mulakat_simulasyon_api.dto.response.MulakatStartResponse;
import com.simulasyon.mulakat_simulasyon_api.entity.*;
import com.simulasyon.mulakat_simulasyon_api.repository.AnswerRepository;
import com.simulasyon.mulakat_simulasyon_api.repository.MulakatSessionRepository;
import com.simulasyon.mulakat_simulasyon_api.repository.QuestionRepository;
import com.simulasyon.mulakat_simulasyon_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MulakatSessionService {
    private final UserRepository userRepository;
    private final MulakatSessionRepository mulakatSessionRepository;
    private final QuestionRepository questionRepository;
    private final OllamaService ollamaService;
    private final AnswerRepository answerRepository;

    public MulakatSessionService(UserRepository userRepository,
                                 MulakatSessionRepository mulakatSessionRepository,
                                 QuestionRepository questionRepository,
                                 OllamaService ollamaService ,
                                 AnswerRepository answerRepository) {
        this.userRepository = userRepository;
        this.mulakatSessionRepository = mulakatSessionRepository;
        this.questionRepository = questionRepository;
        this.ollamaService = ollamaService;
        this.answerRepository = answerRepository;
    }
    @Transactional
    public MulakatStartResponse mulakatStart(Long userId, String technology, String difficulty ,Integer countQuestion) {

        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("Bu ID'ye sahip kullanıcı bulunmuyor"));
        MulakatSession newSession = new MulakatSession();
        newSession.setUser(user);
        newSession.setTechnology(technology);
        newSession.setDifficulty(difficulty);

        if(countQuestion!=null && countQuestion>0){
            newSession.setTotalCountOfQuestion(countQuestion);
        }

        MulakatSession savedSession = mulakatSessionRepository.save(newSession);

        String producedQuestionText = ollamaService.QuestionCreat("llama3",technology,difficulty);

        Question firstQuestion = new Question();
        firstQuestion.setMulakatSession(savedSession);
        firstQuestion.setQuestionText(producedQuestionText);
        questionRepository.save(firstQuestion);

        MulakatStartResponse response = new MulakatStartResponse();
        response.setSessionDetails(savedSession);
        response.setFirstQuestionText(producedQuestionText);

        return response;
    }
    @Transactional
    public AnswerEvaluationResponse answerEvaluation(Long sessionId, Long questionId,String answerText) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(()-> new RuntimeException("Soru Bulunamadi! "+ questionId));

        MulakatSession mulakatSession = mulakatSessionRepository.findById(sessionId)
                .orElseThrow(()-> new RuntimeException("Geçerli Oturum Bulunamadi! "+ sessionId));

        if(!question.getMulakatSession().getId().equals(mulakatSession.getId())) {
            throw new IllegalStateException("Bu soru bu mülakata ait değil!");
        }

        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setAnswerText(answerText);
        answerRepository.save(answer);

        String evaluationResult = ollamaService.answerEvaluate(question.getQuestionText(), answerText,"llama3");

        try{
            String[] parts = evaluationResult.split("\\|\\|\\|");
            if(parts.length>=2){
            String puanStr=parts[0].replace("Puan :","").trim();
            String callback = parts[1].replace("Geri Bildirim :","").trim();

            answer.setPuan(Integer.parseInt(puanStr));
            answer.setCallback(callback);}
            else{
                throw new Exception("Beklenen format bulunamadı");
            }
        }catch(Exception e){
            System.err.println("Ollama cevabı ayrıştırıldı: "+ evaluationResult);
            answer.setPuan(0);
            answer.setCallback("Değerlendirme formatı anlaşılamadı. Ham cevap: " + evaluationResult);
        }

        Answer evaluatedAnswerEntity = answerRepository.save(answer);

        long cevaplananSoruSayisi = answerRepository.countByQuestion_MulakatSession_Id(sessionId);

        String nextQuestionText = null;
        Long nextQuestionId = null;

        if(cevaplananSoruSayisi<mulakatSession.getTotalCountOfQuestion()){
            nextQuestionText =ollamaService.QuestionCreat("llama3", mulakatSession.getTechnology(), mulakatSession.getDifficulty());
            Question newQuestion = new Question();
            newQuestion.setMulakatSession(mulakatSession);
            newQuestion.setQuestionText(nextQuestionText);
            Question savedQuestion = questionRepository.save(newQuestion);
            nextQuestionId = savedQuestion.getId();
        }else {
            mulakatFinish(sessionId);
        }

        Question evaluatedQuestionEntity = evaluatedAnswerEntity.getQuestion();

        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(evaluatedQuestionEntity.getId());
        questionDto.setQuestionText(evaluatedQuestionEntity.getQuestionText());

        AnswerDto answerDto = new AnswerDto();
        answerDto.setId(evaluatedAnswerEntity.getId());
        answerDto.setQuestion(questionDto);
        answerDto.setAnswerText(evaluatedAnswerEntity.getAnswerText());
        answerDto.setPuan(evaluatedAnswerEntity.getPuan());
        answerDto.setCallback(evaluatedAnswerEntity.getCallback());
        answerDto.setAnswerTime(evaluatedAnswerEntity.getAnswerTime());

        AnswerEvaluationResponse finalResponse = new AnswerEvaluationResponse();
        finalResponse.setAnswerEvaluation(answerDto);
        finalResponse.setNextQuestionText(nextQuestionText);
        finalResponse.setNextQuestionId(nextQuestionId);

        return finalResponse;
    }

    @Transactional
    public MulakatSessionDto mulakatFinish (Long sessionId){
        MulakatSession session = mulakatSessionRepository.findById(sessionId)
                .orElseThrow(()-> new RuntimeException("Geçerli Oturum Bulunamadı! ID: " + sessionId));

        if(session.getDurum()== Durum.BITTI){
            throw new IllegalStateException("Bu mülakat oturumu zaten daha önce bitirilmiş.");
        }

        session.setDurum(Durum.BITTI);
        session.setFinishTime(LocalDateTime.now());
        MulakatSession savedSession = mulakatSessionRepository.save(session);

        User userEntity = savedSession.getUser();
        UserDto userDto = new UserDto();
        userDto.setUserId(userEntity.getId());
        userDto.setUserName(userEntity.getNameSurname());
        userDto.setProfileFotoUrl(userEntity.getProfileFotoUrl());

        MulakatSessionDto sessionDto = new MulakatSessionDto();
        sessionDto.setId(savedSession.getId());
        sessionDto.setUser(userDto);
        sessionDto.setTechnology(savedSession.getTechnology());
        sessionDto.setDifficulty(savedSession.getDifficulty());
        sessionDto.setDurum(savedSession.getDurum());
        sessionDto.setStartTime(savedSession.getStartTime());
        sessionDto.setFinishTime(savedSession.getFinishTime());

        return sessionDto;
    }

    public List<MulakatSessionDto> kullanicininMulakatlariniListele(Long userId){

        List<MulakatSession> mulakatSessionList = mulakatSessionRepository.findByUserId(userId);
        List<MulakatSessionDto> mulakatSessionDtoList = new ArrayList<>();

        for(MulakatSession mulakatSessionEntity : mulakatSessionList){
            MulakatSessionDto mulakatSessionDto = new MulakatSessionDto();

            mulakatSessionDto.setId(mulakatSessionEntity.getId());
            mulakatSessionDto.setTechnology(mulakatSessionEntity.getTechnology());
            mulakatSessionDto.setDurum(mulakatSessionEntity.getDurum());
            mulakatSessionDto.setStartTime(mulakatSessionEntity.getStartTime());
            mulakatSessionDto.setFinishTime(mulakatSessionEntity.getFinishTime());
            mulakatSessionDto.setDifficulty(mulakatSessionEntity.getDifficulty());

            User userEntity = mulakatSessionEntity.getUser();
            UserDto userDto = new UserDto();

            userDto.setUserId(userEntity.getId());
            userDto.setUserName(userEntity.getNameSurname());
            userDto.setProfileFotoUrl(userEntity.getProfileFotoUrl());

            mulakatSessionDto.setUser(userDto);

            mulakatSessionDtoList.add(mulakatSessionDto);
        }
        return mulakatSessionDtoList;
    }
}
