package com.fitness.aiService.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiService.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
// generate recommendation
public class ActivityAiService {

    private final GeminiService geminiService;
    public String generateRecommendation(Activity activity){
        String prompt=createPrompt(activity);
        String aiResponse=geminiService.getAnswer(prompt);
        log.info("RESPONSE FROM AI: {} ",aiResponse);
        processAiResponse(activity,aiResponse);
        return aiResponse;
    }

    //processing the AI response
    private void processAiResponse(Activity activity,String aiResponse){
        try{
            ObjectMapper mapper= new ObjectMapper();
            JsonNode rootNode=mapper.readTree(aiResponse);

            JsonNode textNode=rootNode.path("candidates")
                    .get(0)
                    .path("contents")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent =textNode.asText()
                    .replaceAll("```json\\n","")
                    .replaceAll("\\n```","")
                    .trim();
            log.info("PARSED RESPONSE FROM AI: {} ",jsonContent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createPrompt(Activity activity) {
        return """
            You are a professional fitness coach AI.

            Analyze the following user fitness activity and generate a structured response/recommendation.

            Activity Details:
            - User ID: %s
            - Activity Type: %s
            - Duration: %d minutes
            - Calories Burned: %d
            - Start Time: %s
            - Additional Metrics: %s

            Based on this, provide:

            1. A short overall recommendation
            2. Suggestions (list)
            3. Improvements (list)
            4. Safety tips (list)

            IMPORTANT:
            - Return ONLY valid JSON
            - Do NOT add explanation
            - Follow this exact structure:

            {
              "recommendation": "string",
              "suggestions": ["string"],
              "improvements": ["string"],
              "safety": ["string"]
            }
            """
                .formatted(
                        activity.getUserId(),
                        activity.getActivityType() ,
                        activity.getDuration(),
                        activity.getCalorieBurned(),
                        activity.getStartTime(),
                        activity.getAdditionalMetrics()
                );
    }
}
