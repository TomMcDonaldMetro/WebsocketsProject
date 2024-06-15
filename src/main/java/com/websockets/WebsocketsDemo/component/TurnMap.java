package com.websockets.WebsocketsDemo.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class TurnMap {

	public Map<String, Integer> map;
	
	public TurnMap() {
		this.map = new ConcurrentHashMap<>();
	}

	public void setTurn(String user, Integer value) {
		this.map.put(user, value);
	}
	
	public Integer getTurn(String user) {
		return this.map.get(user);
	}
	
	
	
}
