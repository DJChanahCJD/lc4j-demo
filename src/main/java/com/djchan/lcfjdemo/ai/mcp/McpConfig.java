package com.djchan.lcfjdemo.ai.mcp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;

// https://docs.langchain4j.dev/tutorials/mcp
@Configuration
public class McpConfig {

    @Value("${bigmodel.api-key}")
    private String apiKey;
    
    // 传统HTTP/SSE方式，已被废弃
    // https://modelcontextprotocol.io/specification/2024-11-05/basic/transports#http-with-sse     
    // 调用的MCP服务：https://mcp.so/server/zhipu-web-search/BigModel?tab=content
    @Bean
    McpToolProvider mcpToolProvider() {
        // 和 MCP 服务通讯
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("https://open.bigmodel.cn/api/mcp/web_search/sse?Authorization=" + apiKey)
                .logRequests(true) // 开启日志，查看更多信息
                .logResponses(true)
                .build();
        // 创建 MCP 客户端（Server-Sent Events (SSE) ，服务端单向给客户端发送消息）
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .key("testMcpClient")
                .transport(transport)
                .build();
        // 从 MCP 客户端获取工具
        McpToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();
        return toolProvider;
    }
}

