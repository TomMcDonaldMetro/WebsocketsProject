package com.websockets.WebsocketsDemo.services;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Service
public class StompEventListener {


	
	@EventListener
	public void subscribeHandler(SessionSubscribeEvent event) throws Exception {

		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String destination = accessor.getDestination();

		
		String[] split = destination.split("/");
		String roomid = split[split.length-1];

	}
}
