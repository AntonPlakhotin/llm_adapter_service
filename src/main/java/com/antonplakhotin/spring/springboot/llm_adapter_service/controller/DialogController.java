package com.antonplakhotin.spring.springboot.llm_adapter_service.controller;

import com.antonplakhotin.spring.springboot.llm_adapter_service.dto.AskLlmRes;
import com.antonplakhotin.spring.springboot.llm_adapter_service.dto.AskLlmRq;
import com.antonplakhotin.spring.springboot.llm_adapter_service.dto.Message;
import com.antonplakhotin.spring.springboot.llm_adapter_service.dto.Prompt;
import com.antonplakhotin.spring.springboot.llm_adapter_service.service.LlmService;
import com.antonplakhotin.spring.springboot.llm_adapter_service.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dialog")
public class DialogController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private LlmService llmService;

    @Value("${comet.api.key}")
    private String cometApiKey;

    @GetMapping("/test-key")
    public String testKey() {
        return "Key length: " + (cometApiKey != null ? cometApiKey.length() : "NULL") +
                "<br>Key starts with: " + (cometApiKey != null ? cometApiKey.substring(0, Math.min(5, cometApiKey.length())) : "N/A");
    }

    @PostMapping("/ask")
    public ResponseEntity<AskLlmRes> askLlm(@RequestBody AskLlmRq request) {
        List<Message> messageHistory = storageService.getMessages(request.getChatId());

        // Получаем промпт чата
        Prompt chatPrompt = storageService.getPrompt(request.getChatId());
        String systemPrompt = "You are a helpful AI assistant.";
        if (chatPrompt != null && chatPrompt.getText() != null && !chatPrompt.getText().isEmpty()) {
            systemPrompt = chatPrompt.getText();
        }

        // Передаем промпт в LLM
        String aiResponse = llmService.askQwen(
                request.getModel(),
                messageHistory,
                request.getMessage().getContent(),
                systemPrompt
        );

        return ResponseEntity.ok(new AskLlmRes(aiResponse));
    }

    @GetMapping("/chat/{chatId}/prompt")
    public ResponseEntity<Prompt> getChatPrompt(@PathVariable long chatId) {
        Prompt prompt = storageService.getPrompt(chatId);
        return prompt != null ? ResponseEntity.ok(prompt) : ResponseEntity.notFound().build();
    }
}