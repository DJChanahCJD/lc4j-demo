package com.djchan.lcfjdemo.ai.service;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

// 代码助手智能体
// https://docs.langchain4j.dev/tutorials/rag
@AiService(contentRetriever = "embeddingStoreContentRetriever")
public interface EasyRagTestService {

    @SystemMessage(fromResource = "prompts/easy-rag-test.txt")
    String chat(String userMessage);

    @SystemMessage(fromResource = "prompts/easy-rag-test.txt")
    Result<String> chatWithResult(String userMessage);
}
