package com.websockets.WebsocketsDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class WebsocketsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketsDemoApplication.class, args);
	}

}
