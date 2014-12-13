package zz23_jj26.server.message.game;

import java.util.HashMap;

import common.message.chat.IChatMessage;

public class GameOver implements IChatMessage{

	private static final long serialVersionUID = 3259553953170285595L;

	private HashMap<String, Integer> scoreBoard;
	private String winningTeam;
	
	public GameOver(HashMap<String, Integer> scoreBoard, String winningTeam){
		this.setScoreBoard(scoreBoard);
		this.setWinningTeam(winningTeam);
	}

	public HashMap<String, Integer> getScoreBoard() {
		return scoreBoard;
	}

	private void setScoreBoard(HashMap<String, Integer> scoreBoard) {
		this.scoreBoard = scoreBoard;
	}

	public String getWinningTeam() {
		return winningTeam;
	}

	private void setWinningTeam(String winningTeam) {
		this.winningTeam = winningTeam;
	}
	
}
