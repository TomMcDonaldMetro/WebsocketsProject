package com.websockets.WebsocketsDemo.config;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyHandler extends TextWebSocketHandler {

	@Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        
//		System.out.println("handle");
//		System.out.println(session.getPrincipal().getName());
//		System.out.println(message.getPayloadLength());
//		System.out.println(message.getPayload());
		System.out.println("handler" + session.getId());
		session.sendMessage(message);
    }
	
}
