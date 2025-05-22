package org.example.controller;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint("/ws/{username}")
public class WebSocketController {
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        System.out.println("WebSocket 连接开启: " + username);
        sessions.put(username, session);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        System.out.println("WebSocket 收到消息: " + message + " 来自用户: " + username);
        // 推送 layout 数据变化
        sessions.forEach((user, session) -> {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        });
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        System.out.println("WebSocket 连接关闭: " + username);
        sessions.remove(username);
    }
}