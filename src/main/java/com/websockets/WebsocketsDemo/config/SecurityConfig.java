package com.websockets.WebsocketsDemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


	
	@Bean
	public UserDetailsService users() {
	    UserDetails user1 = User.builder()
	        .username("Potter")
	        .password("{noop}pass")
	        .roles("myrole")
	        .build();
	    UserDetails user2 = User.builder()
	        .username("Snape")
	        .password("{noop}pass")
	        .roles("myrole2")
	        .build();
	    UserDetails user3 = User.builder()
		        .username("Jimmy")
		        .password("{noop}pass")
		        .roles("myrole2")
		        .build();
	    return new InMemoryUserDetailsManager(user1, user2, user3);
	}
	
}
