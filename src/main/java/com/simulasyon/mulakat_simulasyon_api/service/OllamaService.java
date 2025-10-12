package com.simulasyon.mulakat_simulasyon_api.service;

import com.simulasyon.mulakat_simulasyon_api.dto.request.OllomaRequest;
import com.simulasyon.mulakat_simulasyon_api.dto.response.OllomaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OllamaService {

    private final WebClient webClient;

    public OllamaService(WebClient.Builder webClientBuilder, @Value("${ollama.api.baseurl}") String ollamaBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(ollamaBaseUrl).build();
    }

    public String QuestionCreat(String model,String technology,String difficulty) {
        String prompt = String.format("Bana %s alanında,bir yazılım mülakatında sorulabilecek %s zorlukta teknik bir soru üret. Sadece sorunun kendisini yaz,başka hiçbir açıklama veya giriş cümlesi ekleme.,",
                technology,difficulty);
        OllomaRequest requestBody = new OllomaRequest(model,prompt,false);
        OllomaResponse ollomaResponse = webClient.post()
                .uri("/api/generate")
                .body(Mono.just(requestBody),OllomaRequest.class)
                .retrieve()
                .bodyToMono(OllomaResponse.class)
                .block();

        if(ollomaResponse!=null){
            return ollomaResponse.getResponse().trim();
        }
        else {
            throw new RuntimeException("Ollama API'sinden geçerli bir cevap alınamadı.");
        }

    }
}
