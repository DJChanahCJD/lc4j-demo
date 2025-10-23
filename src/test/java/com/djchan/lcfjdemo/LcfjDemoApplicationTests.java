package com.djchan.lcfjdemo;

import com.djchan.lcfjdemo.ai.service.EasyRagTestService;
import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import dev.langchain4j.rag.content.Content;

import java.util.List;

@SpringBootTest
class LcfjDemoApplicationTests {

    @Resource
    EasyRagTestService easyRagTestService;

    @Test
    void contextLoads() {
    }

    @Test
    void chatWithRag() {
        Result<String> result = easyRagTestService.chatWithResult("怎么学习 Java？有哪些常见面试题？");
        String content = result.content();
        List<Content> sources = result.sources();   // 文档来源
        System.out.println(content);
        System.out.println(sources);


        // TODO: 测试 NaiveRAG：1. 按需切割文档 2. 补充文件名信息 3. 自定义 Embedding和ContentRetriver

        // TODO: 测试 AdvancedRAG： 高度定制，额外支持查询转换器、查询路由、内容聚合器、内容注入器等特性，将整个RAG的流程流水线化（RAG pipeline)。
        //  AiServices.builder(xxx.class)
        //    ...
        //    .retrievalAugmentor(retrievalAugmentor)
        //    .build();

        // 向量可以持久化到 PG Vector（https://docs.langchain4j.dev/integrations/embedding-stores/pgvector/）
    }



}
