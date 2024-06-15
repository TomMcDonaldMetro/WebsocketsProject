package com.websockets.WebsocketsDemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

	
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// simple broker is the built-in message broker.
		// it will broker subscriptions and broadcasts and route messages whose
		// destination header begins with /topic or /queue to the broker.
		// *for the simple broker, the /topic and /queue prefixes do not have special meaning
		// 		they are just following convention.
		// can send straight to the broker
		config.enableSimpleBroker("/queue", "/topic");
		//onfig.enableStompBrokerRelay(null)
		
	    //config.setUserDestinationPrefix("/user"); // DEFINE HERE
		// STOMP messages whose destination begins with /app are routed to @MessageMapping
		// methods in @Controller classes.
		config.setApplicationDestinationPrefixes("/app");
	}

	// STOMP endpoint HTTP URL which a WebSocket client needs to connect to for the WebSocket handshake. 
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/chat").setAllowedOrigins("*").addInterceptors(new MyHandShakeInterceptor());
	}
	
	// for Jakarta WebSocket Servers
	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
	    ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
	    container.setMaxTextMessageBufferSize(8192);
	    container.setMaxBinaryMessageBufferSize(8192);
	    return container;
	}
	
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new MyChannelInterceptor());
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(myHandler(), "/myHandler").addInterceptors(new MyHandShakeInterceptor());
		
	}
	
	@Bean
	public WebSocketHandler myHandler() {
		return new MyHandler();
	}
	
	
//	@Override
//	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
//		registry.setMessageSizeLimit(4 * 8192);
//		registry.setTimeToFirstMessage(30000);
//	}

}
