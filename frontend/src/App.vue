<template>
  <div class="chat-container">
    <div class="chat-header">
      <h1>AI 编程小助手</h1>
      <div class="chat-id">会话 ID: {{ chatId }}</div>
    </div>
    
    <div class="chat-messages" ref="messagesContainer">
      <div 
        v-for="message in messages" 
        :key="message.id"
        :class="['message', message.sender]"
      >
        <div class="message-content">{{ message.content }}</div>
        <div class="message-time">{{ formatTime(message.timestamp) }}</div>
      </div>
      <div v-if="isLoading" class="message ai">
        <div class="message-content typing">AI 正在回复...</div>
      </div>
    </div>
    
    <div class="chat-input-container">
      <input
        v-model="inputMessage"
        @keyup.enter="sendMessage"
        placeholder="请输入您的问题..."
        :disabled="isLoading"
      />
      <button @click="sendMessage" :disabled="isLoading || !inputMessage.trim()">
        发送
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue';
import type { Message } from './types/chat';
import { ChatService } from './services/chatService';

// 响应式数据
const messages = ref<Message[]>([]);
const inputMessage = ref('');
const isLoading = ref(false);
const chatId = ref('');
const messagesContainer = ref<HTMLElement>();
let currentAiMessageId = '';
let currentAiMessageContent = '';

// 组件挂载时初始化
onMounted(() => {
  // 生成聊天室ID
  chatId.value = ChatService.generateChatId();
  
  // 添加欢迎消息
  addMessage('欢迎使用AI编程小助手！我可以帮您解答编程学习和求职面试相关的问题。', 'ai');
});

// 发送消息
const sendMessage = () => {
  const message = inputMessage.value.trim();
  if (!message || isLoading.value) return;
  
  // 添加用户消息
  addMessage(message, 'user');
  
  // 清空输入框
  inputMessage.value = '';
  
  // 标记为加载中
  isLoading.value = true;
  
  // 初始化AI回复的消息ID和内容
  currentAiMessageId = generateMessageId();
  currentAiMessageContent = '';
  
  // 调用SSE服务
  ChatService.streamChat(
    message,
    (chunk) => {
      // 更新AI消息内容
      currentAiMessageContent += chunk;
      updateAiMessage();
    },
    () => {
      // 完成时的处理
      isLoading.value = false;
    }
  );
};

// 添加消息
const addMessage = (content: string, sender: 'user' | 'ai') => {
  const newMessage: Message = {
    id: generateMessageId(),
    content,
    sender,
    timestamp: new Date()
  };
  
  messages.value.push(newMessage);
  scrollToBottom();
};

// 更新AI消息
const updateAiMessage = () => {
  // 查找当前AI消息或创建新的
  const existingMessageIndex = messages.value.findIndex(msg => msg.id === currentAiMessageId);
  
  if (existingMessageIndex >= 0 && messages.value[existingMessageIndex]) {
    // 更新现有消息
    messages.value[existingMessageIndex].content = currentAiMessageContent;
  } else {
    // 创建新的AI消息
    const newMessage: Message = {
      id: currentAiMessageId,
      content: currentAiMessageContent,
      sender: 'ai',
      timestamp: new Date()
    };
    messages.value.push(newMessage);
  }
  
  scrollToBottom();
};

// 生成消息ID
const generateMessageId = (): string => {
  return Date.now().toString(36) + Math.random().toString(36).substring(2);
};

// 格式化时间
const formatTime = (date: Date): string => {
  return date.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  });
};

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick();
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
  }
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #f5f5f5;
}

.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-width: 1200px;
  margin: 0 auto;
  background-color: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.chat-header {
  padding: 20px;
  background-color: #4a90e2;
  color: white;
  text-align: center;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-header h1 {
  font-size: 24px;
  font-weight: 600;
}

.chat-id {
  font-size: 12px;
  opacity: 0.8;
  background-color: rgba(255, 255, 255, 0.2);
  padding: 4px 12px;
  border-radius: 12px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 18px;
  position: relative;
}

.message.user {
  align-self: flex-end;
  background-color: #4a90e2;
  color: white;
  border-bottom-right-radius: 4px;
}

.message.ai {
  align-self: flex-start;
  background-color: #f1f1f1;
  color: #333;
  border-bottom-left-radius: 4px;
}

.message-content {
  word-wrap: break-word;
  line-height: 1.5;
}

.message-time {
  font-size: 12px;
  opacity: 0.6;
  margin-top: 4px;
  text-align: right;
}

.message.ai .message-time {
  text-align: left;
}

.typing {
  display: inline-block;
  position: relative;
}

.typing::after {
  content: '...';
  animation: typing 1.5s steps(5, end) infinite;
}

@keyframes typing {
  0%, 20% { content: '.'; }
  40% { content: '..'; }
  60% { content: '...'; }
  80%, 100% { content: ''; }
}

.chat-input-container {
  display: flex;
  padding: 20px;
  border-top: 1px solid #eee;
  background-color: #fafafa;
  gap: 12px;
}

.chat-input-container input {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid #ddd;
  border-radius: 24px;
  font-size: 16px;
  outline: none;
  transition: border-color 0.3s;
}

.chat-input-container input:focus {
  border-color: #4a90e2;
}

.chat-input-container input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.chat-input-container button {
  padding: 12px 24px;
  background-color: #4a90e2;
  color: white;
  border: none;
  border-radius: 24px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.chat-input-container button:hover:not(:disabled) {
  background-color: #357abd;
}

.chat-input-container button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chat-header h1 {
    font-size: 20px;
  }
  
  .chat-id {
    display: none;
  }
  
  .message {
    max-width: 85%;
  }
}
</style>
