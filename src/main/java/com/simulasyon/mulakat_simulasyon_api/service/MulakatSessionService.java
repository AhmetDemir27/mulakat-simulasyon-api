package com.simulasyon.mulakat_simulasyon_api.service;

import com.simulasyon.mulakat_simulasyon_api.dto.AnswerDto;
import com.simulasyon.mulakat_simulasyon_api.dto.QuestionDto;
import com.simulasyon.mulakat_simulasyon_api.dto.response.AnswerEvaluationResponse;
import com.simulasyon.mulakat_simulasyon_api.dto.response.MulakatStartResponse;
import com.simulasyon.mulakat_simulasyon_api.entity.Answer;
import com.simulasyon.mulakat_simulasyon_api.entity.MulakatSession;
import com.simulasyon.mulakat_simulasyon_api.entity.Question;
import com.simulasyon.mulakat_simulasyon_api.entity.User;
import com.simulasyon.mulakat_simulasyon_api.repository.AnswerRepository;
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

        String newQuestionText = ollamaService.QuestionCreat("llama3", mulakatSession.getTechnology(), mulakatSession.getDifficulty());

        Question newQuestion = new Question();
        newQuestion.setMulakatSession(mulakatSession);
        newQuestion.setQuestionText(newQuestionText);
        Question updatedQuestion = questionRepository.save(newQuestion);

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
        finalResponse.setNextQuestionText(updatedQuestion.getQuestionText());
        finalResponse.setNextQuestionId(updatedQuestion.getId());

        return finalResponse;
    }
}
