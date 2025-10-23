package com.djchan.lcfjdemo.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

// 代码助手智能体
@AiService()
public interface CodeHelperService {

    @SystemMessage(fromResource = "prompts/code-helper.txt")
    String chat(String message);

    String chat(@MemoryId String memoryId, @UserMessage String message);

}
