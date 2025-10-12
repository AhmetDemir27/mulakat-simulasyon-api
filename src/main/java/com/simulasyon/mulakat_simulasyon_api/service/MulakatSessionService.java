package com.simulasyon.mulakat_simulasyon_api.service;

import com.simulasyon.mulakat_simulasyon_api.dto.response.MulakatStartResponse;
import com.simulasyon.mulakat_simulasyon_api.entity.MulakatSession;
import com.simulasyon.mulakat_simulasyon_api.entity.Question;
import com.simulasyon.mulakat_simulasyon_api.entity.User;
import com.simulasyon.mulakat_simulasyon_api.repository.MulakatSessionRepository;
import com.simulasyon.mulakat_simulasyon_api.repository.QuestionRepository;
import com.simulasyon.mulakat_simulasyon_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MulakatSessionService {
    private final UserRepository userRepository;
    private final MulakatSessionRepository mulakatSessionRepository;
    private final QuestionRepository questionRepository;
    private final OllamaService ollamaService;



    public MulakatSessionService(UserRepository userRepository, MulakatSessionRepository mulakatSessionRepository,QuestionRepository questionRepository, OllamaService ollamaService) {
        this.userRepository = userRepository;
        this.mulakatSessionRepository = mulakatSessionRepository;
        this.questionRepository = questionRepository;
        this.ollamaService = ollamaService;
    }
    @Transactional
    public MulakatStartResponse mulakatStart(Long userId, String technology, String difficulty) {

        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("Bu ID'ye sahip kullanıcı bulunmuyor"));
        MulakatSession newSession = new MulakatSession();
        newSession.setUser(user);
        newSession.setTechnology(technology);
        newSession.setDifficulty(difficulty);
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
}
