package com.djchan.lcfjdemo.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

// 代码助手智能体
@AiService(contentRetriever = "embeddingStoreContentRetriever")
public interface RagTestService {

    @SystemMessage("你是一个代码助手，你可以回答用户的代码问题")
    String chat(String userMessage);
}
