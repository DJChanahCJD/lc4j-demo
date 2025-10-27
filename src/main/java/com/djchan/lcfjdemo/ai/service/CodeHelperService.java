package com.djchan.lcfjdemo.ai.service;

import com.djchan.lcfjdemo.ai.guardrails.SafeInputGuardrail;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.spring.AiService;

// 代码助手智能体
@AiService
@InputGuardrails(SafeInputGuardrail.class)  // 开启输入守卫
public interface CodeHelperService {

    @SystemMessage(fromResource = "prompts/code-helper.txt")
    String chat(String message);

    String simpleChat(String message);

    String chat(@MemoryId String memoryId, @UserMessage String message);

}
