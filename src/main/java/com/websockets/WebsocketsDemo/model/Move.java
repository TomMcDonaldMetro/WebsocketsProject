package com.websockets.WebsocketsDemo.model;

public class Move {
	
	String prev;
	int num;
	String move;
	
	public Move() {
		
	}
	
	public Move(int num, String prev, String move) {
		this.num = num;
		this.prev = prev;
		this.move = move;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getPrev() {
		return prev;
	}

	public void setPrev(String prev) {
		this.prev = prev;
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}
	
	
	
}
