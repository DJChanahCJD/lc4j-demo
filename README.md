# LCFJ-Demo

这是一个基于 LangChain4j 和 Spring Boot 的 AI 编程助手演示项目，提供代码问答、RAG、工具调用、流式输出等功能。

## 技术栈

- **后端**：Spring Boot 3.5.x + Java 21
- **前端**：Vue 3 + TypeScript + Vite
- **AI 框架**：LangChain4j
- **数据库**：MySQL + Redis
- **API文档**：Knife4j (OpenAPI 3)

## 主要功能

- **基础聊天**：基于大语言模型的对话功能
- **代码助手**：编程学习和求职面试相关问题解答
- **RAG检索增强**：基于本地文档的智能问答
- **工具调用**：集成数学计算等工具能力
- **MCP服务**：集成外部能力（如网络搜索）
- **安全护轨**：输入内容安全检测
- **流式输出**：实时展示AI回复内容
- **Redis持久化**：聊天记录持久化存储

## 快速开始

### 后端配置与启动

1. **配置文件准备**
   ```bash
   cp src/main/resources/application-example.yml src/main/resources/application-local.yml
   ```
   编辑 `application-local.yml`，配置必要的参数（如API密钥、数据库连接等）。

2. **启动服务**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **访问API文档**
   浏览器打开：http://localhost:8901/api/doc.html

### 前端配置与启动

1. **安装依赖**
   ```bash
   cd frontend
   npm install
   ```

2. **启动开发服务**
   ```bash
   npm run dev
   ```

## API端点说明

- `/api/chat/text` - 基础聊天功能
- `/api/chat/code` - 代码助手功能
- `/api/chat/stream` - 流式输出功能
- `/api/chat/with-easy-rag` - RAG检索增强功能
- `/api/chat/with-tools` - 工具调用功能
- `/api/chat/with-mcp` - MCP服务集成
- `/api/chat/with-guardrails` - 安全护轨功能

## 目录结构

```
├── src/main/java/com/djchan/lcfjdemo/  # 后端源码
│   ├── ai/          # AI相关功能模块
│   ├── controller/  # API控制器
│   └── LcfjDemoApplication.java  # 应用入口
├── src/main/resources/  # 配置和资源文件
│   ├── prompts/     # 提示词模板
│   └── rag-docs/    # RAG文档目录
└── frontend/        # 前端源码
```

## 注意事项

1. 确保配置有效的API密钥（如OpenAI或其他兼容模型）
2. RAG功能需要在 `src/main/resources/rag-docs` 目录下放置文档
3. Redis用于聊天记忆持久化，需要确保Redis服务可用


> ⚠️ 当前各个AiService 没有做RAG的隔离（Langchain4j似乎会给每个AiService自动注入ContentRetriever），暂时不知道如何解决
