package com.antonplakhotin.spring.springboot.llm_adapter_service.service;

import com.antonplakhotin.spring.springboot.llm_adapter_service.dto.Message;
import com.antonplakhotin.spring.springboot.llm_adapter_service.dto.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class StorageService {

    @Autowired
    private RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8099/api";

    public List<Message> getMessages(long chatId) {
        try {
            String url = baseUrl + "/" + chatId + "/messages";

            ResponseEntity<List<Message>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Message>>() {}
            );

            List<Message> messages = response.getBody();
            return messages != null ? messages : Collections.emptyList();

        } catch (Exception e) {
            System.err.println("Error fetching messages for chatId " + chatId + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Prompt getPrompt(long chatId) {
        try {
            String url = baseUrl + "/chatPrompt/" + chatId;

            ResponseEntity<Prompt> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Prompt>() {}
            );

            return response.getBody();

        } catch (Exception e) {
            System.err.println("Error fetching prompt for chatId " + chatId + ": " + e.getMessage());
            return null;
        }
    }
}
