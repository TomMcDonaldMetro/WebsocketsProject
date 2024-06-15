package com.websockets.WebsocketsDemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.websockets.WebsocketsDemo.component.ChannelMap;
import com.websockets.WebsocketsDemo.component.GameMap;
import com.websockets.WebsocketsDemo.component.TurnMap;
import com.websockets.WebsocketsDemo.model.State;
import com.websockets.WebsocketsDemo.model.TicTacToe;

@Service
public class TicTacToeService implements GameService {

	@Autowired
	GameMap gameMap;
	
	@Autowired
	ChannelMap channelMap;
	
	@Autowired
	TurnMap turnMap;
		
	public TicTacToeService() {
		System.out.println("Spring created a service");
		
	}
	
	@Override
	public String placeMove(String room, int loc) {
		
		TicTacToe tictactoe = gameMap.getGame(room);
		gameMap.getGame(room).placeMark(loc);
		
		if(tictactoe.isOver()) {
			setState(room, State.OVER);
		}
		
		return tictactoe.getBoard().toString();
	}

	@Override
	public State getState(String room) {
		return gameMap.getGame(room).getCurrentState();
	}

	@Override
	public void setState(String room, State state) {

		gameMap.getGame(room).setCurrentState(state);
	}


	@Override
	public void configureChannel(String channel) {
		
		if(gameMap.getGame(channel) == null) {
			gameMap.setGame(channel, new TicTacToe());
		}
		
		if(channelMap.get(channel).size() == 2) {
			int i = 0;
			for (String user : channelMap.getChannelMap().get(channel)) {
				turnMap.setTurn(user, i);
				i++;
			}
		}
		
		//System.out.println("Turn map should have been set: " + turnMap.map.toString());
		
	}

	public ChannelMap getChannelMap() {
		return this.channelMap;
	}


	
	
	

}
