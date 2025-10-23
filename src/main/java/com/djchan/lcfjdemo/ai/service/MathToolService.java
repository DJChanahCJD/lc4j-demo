package com.djchan.lcfjdemo.ai.service;

import dev.langchain4j.service.spring.AiService;


@AiService(tools = "MathTool")
public interface MathToolService {
    String chat(String message);
}
