package com.websockets.WebsocketsDemo.component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.websockets.WebsocketsDemo.model.TicTacToe;

@Component
public class GameMap {

	private Map<String, TicTacToe> map;

	
	
	public GameMap() {
		this.map = new ConcurrentHashMap<String, TicTacToe>();

	}
	
	

	public TicTacToe getGame(String room) {

		return map.get(room);
	}

	public void setGame(String roomname, TicTacToe game) {
		map.put(roomname, game);
	}
	
	public Map<String, TicTacToe> getMap(){
		return this.map;
	}

	

}
