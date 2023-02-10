package ca.mcgill.splendorserver.models;

public class Board {
	
	private TokenBank boardTokens;
	private CardBank cards;
	
	public Board() {
		boardTokens = new TokenBank();
		cards = new CardBank();
	}
	
}
