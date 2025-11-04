package com.antonplakhotin.spring.springboot.llm_adapter_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AskLlmRq {
    private String model;
    private long chatId;
    private Message message;
}
