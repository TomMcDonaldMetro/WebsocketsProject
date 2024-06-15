package com.websockets.WebsocketsDemo.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;


public class MyChannelInterceptor implements ChannelInterceptor {

	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		return message;
	}
	
	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
		
		ChannelInterceptor.super.postSend(message, channel, sent);
	}
	
}
