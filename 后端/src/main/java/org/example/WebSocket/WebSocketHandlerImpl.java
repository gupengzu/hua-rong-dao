package org.example.WebSocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandlerImpl extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = (String) session.getAttributes().get("username");
        System.out.println("WebSocket 连接开启: " + username);
        sessions.put(username, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("WebSocket 收到消息: " + payload);

        // 推送消息给所有连接的客户端
        for (WebSocketSession s : sessions.values()) {
            // 打印s信息
            System.out.println("打印WebSocket session: " + s.getId() + ", isOpen: " + s.isOpen());
            if (s.isOpen()) {
                System.out.println("发送消息给: " + s.getId());
                s.sendMessage(new TextMessage(payload));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = (String) session.getAttributes().get("username");
        System.out.println("WebSocket 连接关闭: " + username);
        sessions.remove(username);
    }
}
