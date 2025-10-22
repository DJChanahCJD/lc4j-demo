package com.djchan.lcfjdemo.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

// 代码助手智能体
@AiService
public interface FriendService {

    @SystemMessage(fromResource = "prompts/friend.txt")
    String chat(String userMessage);
}
