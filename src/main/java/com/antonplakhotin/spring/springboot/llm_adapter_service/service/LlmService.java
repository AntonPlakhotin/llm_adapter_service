package com.antonplakhotin.spring.springboot.llm_adapter_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.antonplakhotin.spring.springboot.llm_adapter_service.dto.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LlmService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${comet.api.key}")
    private String cometApiKey;

    private final String COMET_URL = "https://api.cometapi.com/v1/chat/completions";

    public String askQwen(String model, List<Message> history, String userMessage, String systemPrompt) {
        try {
            List<Map<String, String>> messages = new ArrayList<>();

            // Используем переданный systemPrompt
            messages.add(Map.of("role", "system", "content", systemPrompt));

            for (Message msg : history) {
                String role = msg.getAuthor().name().equalsIgnoreCase("USER") ? "user" : "assistant";
                messages.add(Map.of("role", role, "content", msg.getContent()));
            }

            messages.add(Map.of("role", "user", "content", userMessage));

            Map<String, Object> body = new HashMap<>();
            body.put("model", model != null ? model : "qwen3-8b");
            body.put("messages", messages);
            body.put("temperature", 0.7);
            body.put("max_tokens", 512);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(cometApiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    COMET_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> msg = (Map<String, Object>) choices.get(0).get("message");
                    return (String) msg.get("content");
                }
            }

            return "Ошибка: пустой ответ от модели";

        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка при вызове LLM API: " + e.getMessage();
        }
    }
}