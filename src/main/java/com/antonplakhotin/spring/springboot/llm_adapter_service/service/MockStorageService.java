package com.antonplakhotin.spring.springboot.llm_adapter_service.service;

import com.antonplakhotin.spring.springboot.llm_adapter_service.dto.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.antonplakhotin.spring.springboot.llm_adapter_service.dto.Author.BOT;
import static com.antonplakhotin.spring.springboot.llm_adapter_service.dto.Author.USER;

@Service
public class MockStorageService {
    public List<Message> getMessagesByChatId(String chatId) {
        List<Message> mockMessages = new ArrayList<>();
        mockMessages.add(new Message(1, 1, USER, "Привет!"));
        mockMessages.add(new Message(2, 1, BOT, "Здравствуйте!"));

        return mockMessages;
    }
}
