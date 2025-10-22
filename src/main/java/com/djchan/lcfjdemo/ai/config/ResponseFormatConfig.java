package com.djchan.lcfjdemo.ai.config;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static dev.langchain4j.model.chat.request.ResponseFormatType.JSON;

@Configuration
public class ResponseFormatConfig {

    // AI响应JSON
    @Bean
    public ResponseFormat responseFormat() {
        ResponseFormat responseFormat = ResponseFormat.builder()
                .type(JSON)
                .jsonSchema(JsonSchema.builder()
                        .name("Person")
                        .rootElement(JsonObjectSchema.builder()
                                .addStringProperty("name")
                                .addIntegerProperty("age")
                                .addNumberProperty("height")
                                .addBooleanProperty("married")
                                .required("name", "age", "height", "married")
                                .build())
                        .build())
                .build();
        // ChatRequest chatRequest = ChatRequest.builder()
        //         .responseFormat(responseFormat)
        //         .messages(List.of())
        //         .build();
        return responseFormat;

    }
}
