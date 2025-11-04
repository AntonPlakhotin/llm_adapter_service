package com.antonplakhotin.spring.springboot.llm_adapter_service.controller;

import com.antonplakhotin.spring.springboot.llm_adapter_service.dto.AskLlmRes;
import com.antonplakhotin.spring.springboot.llm_adapter_service.dto.AskLlmRq;
import com.antonplakhotin.spring.springboot.llm_adapter_service.dto.Message;
import com.antonplakhotin.spring.springboot.llm_adapter_service.dto.Prompt;
import com.antonplakhotin.spring.springboot.llm_adapter_service.service.MockStorageService;
import com.antonplakhotin.spring.springboot.llm_adapter_service.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dialog")
public class DialogController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/ask")
    public ResponseEntity<AskLlmRes> askLlm(@RequestBody AskLlmRq request) {
        List<Message> messageHistory = storageService.getMessages(request.getChatId());
        String responseContent = "Привет, в истории " + messageHistory.size() + " сообщений";

        return ResponseEntity.ok(new AskLlmRes(responseContent));
    }

    @GetMapping("/chat/{chatId}/prompt")
    public ResponseEntity<Prompt> getChatPrompt(@PathVariable long chatId) {
            Prompt prompt = storageService.getPrompt(chatId);
            return prompt != null ? ResponseEntity.ok(prompt) : ResponseEntity.notFound().build();
}
}
