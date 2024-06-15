package com.websockets.WebsocketsDemo.controller;

import java.security.Principal;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import com.websockets.WebsocketsDemo.component.ChannelMap;
import com.websockets.WebsocketsDemo.component.GameMap;
import com.websockets.WebsocketsDemo.component.TurnMap;
import com.websockets.WebsocketsDemo.model.Message;
import com.websockets.WebsocketsDemo.model.Move;
import com.websockets.WebsocketsDemo.model.Reply;
import com.websockets.WebsocketsDemo.model.ReplyBoard;
import com.websockets.WebsocketsDemo.model.State;
import com.websockets.WebsocketsDemo.model.TicTacToe;
import com.websockets.WebsocketsDemo.services.TicTacToeService;

@Controller
public class ReplyController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private SimpUserRegistry simpUserRegistry;

	@Autowired
	public GameMap gameMap;
	
	@Autowired
	TicTacToeService tservice;
	
	@Autowired
	TurnMap turnMap;
	
	@Autowired 
	ChannelMap channelMap;


	public Set<SimpUser> getUsers() {
		return simpUserRegistry.getUsers();
	}

	@MessageMapping("/game/{roomname}")
	@SendTo("/queue/{roomname}")
	public Reply privateRoom(Principal principal, Message message, SimpMessageHeaderAccessor accessor,
			StompHeaderAccessor ac) throws Exception {

		String me = message.getMessage().equals("has joined.") || message == null ? "join" : message.getMessage();

		if(me.equals("join")) {
			
			String room = (String) accessor.getSessionAttributes().get("room");

			tservice.getChannelMap().addChannel(room, principal.getName());
			tservice.configureChannel(room); 
			
			System.out.println(tservice.getChannelMap().channelMap.toString());
			if(tservice.getChannelMap().get(room).size() == 2) {
				System.out.println("two players have entered");
				simpMessagingTemplate.convertAndSend("/queue/"+room, new Reply("rgtbegin"));
				if(tservice.getState(room) == State.OVER) {
					simpMessagingTemplate.convertAndSend("/queue/game/"+room, new ReplyBoard(gameMap.getGame(room).getBoard(), -1, "", ""));
				} else {
					tservice.setState(room, State.PLAYING);
				}
				System.out.println(tservice.getState(room).toString());
			}
			
		}
		
		
		return new Reply(principal.getName() + " " + HtmlUtils.htmlEscape(message.getMessage()));

 
	}
	
	@MessageMapping("/game/move/{roomname}")
	@SendTo("/queue/game/{roomname}")
	public ReplyBoard receivedMove(Principal principal, Move move, SimpMessageHeaderAccessor accessor) throws Exception {

		
		String room = (String) accessor.getSessionAttributes().get("room");

		System.out.println(tservice.getState(room));
		if(tservice.getState(room) == State.PLAYING && turnMap.getTurn(principal.getName()) == gameMap.getGame(room).getTheCurrentPlayer()) {
			TicTacToe game = gameMap.getGame(room);
			game.placeMark(move.getNum());
			
			String piece = game.getTheCurrentPlayer() == 0 ? "X" : "O";
			System.out.println(piece);
			if(tservice.getState(room) == State.OVER) {
				piece = piece.equals("X") ? "O" : "X";
				simpMessagingTemplate.convertAndSend("/queue/game/"+room, new ReplyBoard(game.getBoard(), move.getNum(), piece, principal.getName() + " has played " + move.getNum()));
				simpMessagingTemplate.convertAndSend("/queue/"+room, new Reply("Game Over! " + principal.getName() + " is the winner!"));
				
				return null;
			}
			return new ReplyBoard(game.getBoard(), move.getNum(), piece ,principal.getName() + " has played " + move.getNum());
 
		} else {
			System.out.println("not your turn do nothing");
		}
		
		  

		
		return new ReplyBoard();

	}
	
	@MessageMapping("/{roomname}")
	public void startup(Principal principal, Message message, SimpMessageHeaderAccessor accessor) throws InterruptedException {
		// possible placebo need to figure out how Spring instantiates
		// but it seems to give Spring enough time to instantiate the dependencies
		// otherwise it could error depending on the thread execution order.
		Thread.sleep(500);
		String room = (String) accessor.getSessionAttributes().get("room");

		tservice.getChannelMap().addChannel(room, principal.getName());

		System.out.println("join message received from the user");
		String username = principal.getName();
		simpMessagingTemplate.convertAndSendToUser(username, "/queue/reply", new ChannelMap((ConcurrentHashMap<String, Set<String>>) channelMap.getChannelMap()));
 
	}
	

	@MessageMapping("/connected")
	public void clientConnected(Principal principal, Message message, SimpMessageHeaderAccessor accessor) throws InterruptedException {
		Thread.sleep(500);

		System.out.println("join message received from the user");
		String username = principal.getName();
		simpMessagingTemplate.convertAndSendToUser(username, "/queue/reply", new ChannelMap((ConcurrentHashMap<String, Set<String>>) channelMap.getChannelMap()));
 
	}
	


}
