package com.websockets.WebsocketsDemo.services;

import com.websockets.WebsocketsDemo.model.State;

public interface GameService {

	
	
	// manage the Maps pertaining to a game.
	// when the user subscribes to a game channel he should be added to a map
	//	potentially: get(gameroom) => {player1, player2}
	// 	when: gameroom has two players => associate them with the same game reference.
	
	
	// place move
	// update board and return the updated board for the view
	public String placeMove(String user, int loc);
	
	// get state
	// return the current state of the game: waiting, playing, over
	public State getState(String user);
	
	// set state
	// when the state changes
	public void setState(String user, State state);
	
	
	public void configureChannel(String channel);
	
	
}
