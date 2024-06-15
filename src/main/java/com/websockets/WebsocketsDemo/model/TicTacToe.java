package com.websockets.WebsocketsDemo.model;


public class TicTacToe {
	/**
	 * 
	 */
	static final int NUM_SPACES = 9; // total number of moves possible
	private int turn;
	private State currentState;
	
	public enum Mark {
		X, O;
		
		public String getMark() {
			return name();
		}
	}
	
	
	enum WinLocs {
		LOC(new int[][] {{0, 1, 2},
						 {3, 4, 5},
						 {6, 7, 8},
						 {0, 3, 6},
						 {1, 4, 7},
						 {2, 5, 8},
						 {0, 4, 8},
						 {2, 4, 6}});
		private int locs[][];
		WinLocs(int[][] locs){
			this.locs = locs;
		}
		
	}

	private Mark[] board;
	private int[] winLoc; // stores location to highlight in CSS when a win is detected. TODO unimplemented.
	public TicTacToe() {
		board = new Mark[NUM_SPACES];
		winLoc = new int[NUM_SPACES];
		turn = 0;
		currentState = State.WAITING;
	}

	
	
	public State getCurrentState() {
		return currentState;
	}



	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}



	/**
	 * Determines if the game has ended by checking for a winner or the max turns has been reached.
	 * 
	 * @return
	 */
	public boolean isOver() {
		return isWon() || turn == NUM_SPACES;
	}

	/**
	 * Checks the 2d array WinLocs for possible victory points. If one is discovered
	 * setWinLoc takes the winning array, and method returns true.
	 * 
	 * @return
	 */
	private boolean isWon() {
		for (WinLocs locs : WinLocs.values()) {
			for (int[] loc : locs.locs) {
				// if a == b and b == c then a == c and thats 3 in a row for a win
				if(board[loc[0]] != null && board[loc[0]] == board[loc[1]] && board[loc[1]] == board[loc[2]]) {
					setWinLoc(loc);
					setCurrentState(State.OVER);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * The winning location is provided when the game detects a winner.
	 * Winning locations will be highlighted on the view.
	 * 
	 * @param loc
	 */
	private void setWinLoc(int[] loc) {
		this.winLoc = loc;
	}
	
	public int[] getWinLoc() {
		return this.winLoc;
	}

	/**
	 * Receives loc from the Servlet when user makes a move.
	 * 
	 * @param loc
	 * @return
	 */
	public boolean placeMark(int loc) {

		return placeMark(getCurrentPlayer(), loc);
	}

	/**
	 * Checks for valid move and if this move doesn't finish game, turn is incremented.
	 * Current player is determined by the turn, and loc is provided from a user click.
	 * 
	 * @param currentPlayer
	 * @param loc
	 * @return
	 */
	private boolean placeMark(Mark currentPlayer, int loc) {
		// check if the move is valid
		if ((board[loc] == null && currentPlayer != null)) {
			// set the move on the board
			board[loc] = currentPlayer;
			
			if(!isOver()) {
				turn++;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the currentPlayer as winner.
	 *  Turn will not update if game ends.
	 * 
	 * @return
	 */
	public Mark getWinner() {
		if(isWon()) {
			return getCurrentPlayer();
		}
		else {
			return null;
		}
	}

	/**
	 * Determines current player by turn number.
	 * X moves first.
	 * 
	 * @return
	 */
	private Mark getCurrentPlayer() {
		return turn % 2 == 0 ? Mark.X : Mark.O;
	}
	
	public int getTheCurrentPlayer() {
		return turn % 2 == 0 ? 0 : 1;
	}
	
	// 0 x
	// 1 o
	// 2 x
	// 3 o
	// 4 x but places as o

	/**
	 * Returns the game board array.
	 * 
	 * @return
	 */
	public Mark[] getBoard() {
		return this.board;
	}



	public int getTurn() {
		return turn;
	}


	
}
