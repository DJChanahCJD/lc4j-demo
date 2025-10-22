package com.djchan.lcfjdemo.ai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

@Configuration
public class ChatMemoryConfig {
    
    private final RedisChatMemoryStore redisChatMemoryStore;
    
    public ChatMemoryConfig(RedisChatMemoryStore redisChatMemoryStore) {
        this.redisChatMemoryStore = redisChatMemoryStore;
    }
    
    @Bean
    ChatMemoryProvider chatMemoryProvider() {
        // 为每个 memoryId 创建一个独立的聊天记忆，最多保留20条消息，并使用Redis进行持久化
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId) // 重要：为每个记忆设置唯一ID
                .maxMessages(20)
                .chatMemoryStore(redisChatMemoryStore) // 使用Redis持久化存储
                .build();
    }

}