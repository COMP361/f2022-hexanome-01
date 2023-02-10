package ca.mcgill.splendorserver.models;

public class Board {
	
	private TokenBank boardTokens;
	private CardBank cards;
	
	public Board(String[] playerList) {
		boardTokens = new TokenBank(playerList.length + (playerList.length == 4 ? 3:2));
		cards = new CardBank();
	}
	
}
