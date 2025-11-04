package com.antonplakhotin.spring.springboot.llm_adapter_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Prompt {

    private long id;
    private String title;
    private String authorId;
    private String description;
    private String text;
}
