package com.djchan.lcfjdemo.ai.listener;

import dev.langchain4j.observability.api.event.AiServiceErrorEvent;
import dev.langchain4j.observability.api.listener.AiServiceErrorListener;
import dev.langchain4j.invocation.InvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * AI服务错误事件监听器，用于记录AI服务调用失败的情况
 * 实现了LangChain4j的AiServiceErrorListener接口
 */
@Component
public class AiServiceErrorLogger implements AiServiceErrorListener {

    private static final Logger logger = LoggerFactory.getLogger(AiServiceErrorLogger.class);

    /**
     * 当AI服务调用发生错误时触发
     * @param event AI服务错误事件，包含调用上下文和错误信息
     */
    @Override
    public void onEvent(AiServiceErrorEvent event) {
        InvocationContext invocationContext = event.invocationContext();
        Throwable error = event.error();

        // 获取调用上下文信息
        UUID invocationId = invocationContext.invocationId();
        String aiServiceInterfaceName = invocationContext.interfaceName();
        String aiServiceMethodName = invocationContext.methodName();
        List<Object> aiServiceMethodArgs = invocationContext.methodArguments();
        Object chatMemoryId = invocationContext.chatMemoryId();
        Instant eventTimestamp = invocationContext.timestamp();

        // 记录错误信息，使用ERROR级别
        logger.error("AI Service调用失败 - ID: {}, 接口: {}, 方法: {}, 时间: {}",
                invocationId,
                aiServiceInterfaceName,
                aiServiceMethodName,
                eventTimestamp);
        
        // 记录方法参数
        logger.debug("调用参数: {}", aiServiceMethodArgs);
        
        // 记录聊天内存ID（如果存在）
        if (chatMemoryId != null) {
            logger.debug("聊天内存ID: {}", chatMemoryId);
        }
        
        // 记录错误详情
        logger.error("错误信息: {}", error.getMessage(), error);
    }
}