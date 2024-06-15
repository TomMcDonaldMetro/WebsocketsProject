package com.websockets.WebsocketsDemo.component;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;


@Component
public class WebSocketListener {

	@Autowired
	ChannelMap channelMap;
	
	
	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		System.out.println("Connection detected");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
	}

	@EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("disconnect detected");
        Principal principal = headerAccessor.getUser();
        String user = principal.getName().toString();
        String room = headerAccessor.getSessionAttributes().get("room").toString();
        
        // if we are removing a user from a channel of size 1
        if(channelMap.get(room) != null ) {
        	if(channelMap.get(room).size() == 1) {
            	channelMap.remove(room);
            } else {
            	channelMap.get(room).remove(user);
            }
        }
        
	}
	
	@EventListener
	public void handleSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		
		String destination = headerAccessor.getHeader("simpDestination").toString();
		String[] d = destination.split("/");
		String user = headerAccessor.getUser().getName().toString();
		System.out.println(user + " has subscribed to: " + destination);

		Map<String, Object> attributes = headerAccessor.getSessionAttributes();
		if(!d[d.length-1].equals("reply")) {
			attributes.put("subscribed", destination);
			
			attributes.put("room", d[d.length-1]);
		}
	}
}
