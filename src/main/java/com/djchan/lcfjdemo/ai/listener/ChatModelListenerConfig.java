package com.djchan.lcfjdemo.ai.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ChatModelListenerConfig {
    
    // https://docs.langchain4j.dev/tutorials/observability#chat-model-observability
    @Bean
    ChatModelListener chatModelListener() {
        return new ChatModelListener() {
            @Override
            public void onRequest(ChatModelRequestContext requestContext) {
                log.info("onRequest(): {}", requestContext.chatRequest());
            }

            @Override
            public void onResponse(ChatModelResponseContext responseContext) {
                ChatResponse chatResponse = responseContext.chatResponse();

                AiMessage aiMessage = chatResponse.aiMessage();
                System.out.println(aiMessage);

                ChatResponseMetadata metadata = chatResponse.metadata();
                System.out.println(metadata.id());
                
                log.info("onResponse(): {}", chatResponse);
            }

            @Override
            public void onError(ChatModelErrorContext errorContext) {
                log.info("onError(): {}", errorContext.error().getMessage());
            }
        };
    }
}

