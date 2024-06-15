package com.websockets.WebsocketsDemo.model;

import com.websockets.WebsocketsDemo.model.TicTacToe.Mark;

public class ReplyBoard {

	Mark[] board;
	int val;
	String move;
	String message;
	
	public ReplyBoard() {
		 
	}

	public ReplyBoard(Mark[] board, int val, String move, String message) {
		this.board = board;
		this.val = val;
		this.move = move;
		this.message = message;
	}
	
	public ReplyBoard(String message) {
		this.message = message;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMove(String move) {
		this.move = move;
	}

	public String getMove() {
		return move;
	}

	public Mark[] getBoard() {
		return board;
	}

	public void setBoard(Mark[] board) {
		this.board = board;
	}
	
	
	
}
