// 聊天服务 - 处理SSE通信

export class ChatService {
  // 生成唯一的聊天室ID
  static generateChatId(): string {
    return Date.now().toString(36) + Math.random().toString(36).substring(2);
  }

  // 使用SSE方式进行流式聊天
  static streamChat(message: string, onMessageChunk: (chunk: string) => void, onComplete?: () => void): EventSource {
    // 创建EventSource连接到后端的SSE端点（使用代理路径）
    const eventSource = new EventSource(`http://localhost:8901/api/chat/stream?message=${encodeURIComponent(message)}`);
    
    // 处理接收到的消息块
    eventSource.onmessage = (event) => {
      // 检查是否是SSE消息格式
      if (event.data && event.data.startsWith('data:')) {
        // 提取消息内容
        const chunk = event.data.replace('data:', '').trim();
        if (chunk) {
          onMessageChunk(chunk);
        }
      }
    };

    // 处理连接关闭
    eventSource.close = () => {
      if (onComplete) {
        onComplete();
      }
    };

    // 处理错误
    eventSource.onerror = (error) => {
      console.error('SSE连接错误:', error);
      eventSource.close();
      if (onComplete) {
        onComplete();
      }
    };

    return eventSource;
  }
}