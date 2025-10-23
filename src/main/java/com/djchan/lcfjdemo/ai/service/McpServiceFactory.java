package com.djchan.lcfjdemo.ai.service;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServiceFactory {
    @Resource
    private McpToolProvider mcpToolProvider;

    @Bean
    public McpService mcpService(ChatModel chatModel) {
        // 构造 AI Service
        return AiServices.builder(McpService.class)
                .chatModel(chatModel)
                .toolProvider(mcpToolProvider) // MCP 工具调用
                .build();
    }
}
