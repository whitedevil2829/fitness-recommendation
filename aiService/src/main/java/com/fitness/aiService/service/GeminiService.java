package com.fitness.aiService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {
    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiAPIKey;

    public GeminiService(WebClient.Builder webClientBuilder){
        this.webClient=webClientBuilder.build();
    }

    public String getAnswer(String question){
        Map<String,Object> requestBody=Map.of(
                "contents",new Object[] {
                        Map.of(
                                "parts",new Object[] {
                                        Map.of("text",question)
                                }
                        )
                }
        );
        String response= webClient.post()
                .uri(geminiApiUrl+geminiAPIKey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
