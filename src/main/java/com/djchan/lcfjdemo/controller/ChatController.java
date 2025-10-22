package com.djchan.lcfjdemo.controller;

import com.djchan.lcfjdemo.ai.service.RagTestService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.djchan.lcfjdemo.ai.config.RedisChatMemoryStore;
import com.djchan.lcfjdemo.ai.service.CodeHelperService;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final RagTestService ragTestService;
    ChatModel chatModel;
    CodeHelperService codeHelperService;
    RedisChatMemoryStore redisChatMemoryStore;

    public ChatController(ChatModel chatModel, CodeHelperService friendService,
                          RedisChatMemoryStore redisChatMemoryStore, RagTestService ragTestService) {
        this.chatModel = chatModel;
        this.codeHelperService = friendService;
        this.redisChatMemoryStore = redisChatMemoryStore;
        this.ragTestService = ragTestService;
    }

    private static final String SYSTEM_MESSAGE = """
            你是编程领域的小助手，帮助用户解答编程学习和求职面试相关的问题，并给出建议。重点关注 4 个方向：
            1. 规划清晰的编程学习路线
            2. 提供项目学习建议
            3. 给出程序员求职全流程指南（比如简历优化、投递技巧）
            4. 分享高频面试题和面试技巧
            请用简洁易懂的语言回答，助力用户高效学习与求职。
            """;

    @GetMapping("/text")
    public String chat(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        SystemMessage systemMessage = SystemMessage.from(SYSTEM_MESSAGE);
        UserMessage userMessage = UserMessage.from(message);
        ChatResponse chatResponse = chatModel.chat(systemMessage, userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        log.info("AI 输出：" + aiMessage.toString());
        return aiMessage.text();
    }

    @GetMapping("/code")
    public String codeHelper(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        String answer = codeHelperService.chat(message);
        log.info("AI 输出：" + answer);
        return answer;
    }

    @GetMapping("/test")
    public String testCodeHelper() {
        String result = codeHelperService.chat("你好，我是DJChan");
        log.info("AI 输出：" + result);
        result = codeHelperService.chat("我是谁？");
        log.info("AI 输出：" + result);
        return result;
    }

    @GetMapping("/code/memory")
    public String codeHelperbyMemory(@RequestParam String memoryId, @RequestParam String message) {
        // 对于同一个memoryId，对话会有连续的上下文
        // 对于不同的memoryId，对话上下文是隔离的
        return codeHelperService.chat(memoryId, message);
    }

    // 获取聊天记录
    @GetMapping("/memory")
    public String codeHelperbyMemoryHistory(@RequestParam String memoryId) {
        // 获取指定memoryId的聊天记录
        List<ChatMessage> messages = redisChatMemoryStore.getMessages(memoryId);
        // 转换为字符串
        return messages.stream()
                .map(msg -> msg.toString())
                .toList()
                .toString();
    }

    @GetMapping("/with-rag")
    public String ChatwithRag(@RequestParam String message) {
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/rag-docs");
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        String answer = ragTestService.chat(message);
        log.info("AI 输出：" + answer);
        return answer;
    }

}
