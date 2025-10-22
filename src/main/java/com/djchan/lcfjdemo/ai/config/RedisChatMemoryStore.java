package com.djchan.lcfjdemo.ai.config;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;

/**
 * Redis实现的ChatMemoryStore，用于持久化存储聊天消息
 */
@Slf4j
@Component
public class RedisChatMemoryStore implements ChatMemoryStore {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REDIS_KEY_PREFIX = "lcfj:chat:memory:";
    private static final Duration EXPIRATION_TIME = Duration.ofHours(24); // 消息保存24小时

    public RedisChatMemoryStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        log.debug("从Redis获取聊天消息，memoryId: {}", memoryId);
        try {
            String key = getRedisKey(memoryId);
            String val = redisTemplate.opsForValue().get(key);
            if (val != null) {
                return messagesFromJson(val);
            }
        } catch (Exception e) {
            log.error("从Redis获取聊天消息失败，memoryId: {}", memoryId, e);
        }
        return List.of();
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        log.debug("更新Redis中的聊天消息，memoryId: {}, 消息数量: {}", memoryId, messages.size());
        try {
            String key = getRedisKey(memoryId);
            String jsonVal = messagesToJson(messages);

            redisTemplate.opsForValue().set(key, jsonVal, EXPIRATION_TIME);
        } catch (Exception e) {
            log.error("更新Redis中的聊天消息失败，memoryId: {}", memoryId, e);
        }
    }

    @Override
    public void deleteMessages(Object memoryId) {
        log.debug("删除Redis中的聊天消息，memoryId: {}", memoryId);
        try {
            String key = getRedisKey(memoryId);
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("删除Redis中的聊天消息失败，memoryId: {}", memoryId, e);
        }
    }

    /**
     * 获取Redis存储的键名
     */
    private String getRedisKey(Object memoryId) {
        return REDIS_KEY_PREFIX + Objects.requireNonNullElse(memoryId, "default");
    }
}