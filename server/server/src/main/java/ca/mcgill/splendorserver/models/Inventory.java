package ca.mcgill.splendorserver.models;

public class Inventory {
	
	private TokenBank tokens;
	private TokenBank bonus;
	
	public Inventory() {
		tokens = new TokenBank();
		bonus = new TokenBank();
	}
	
	public boolean addTokens(String[] acquiredTokens) {
		return tokens.addAll(acquiredTokens);
	}

}
