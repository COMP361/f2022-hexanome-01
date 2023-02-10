package ca.mcgill.splendorserver.models;

public class Inventory {
	
	private TokenBank tokens;
	private TokenBank bonus;
	
	public boolean addTokens(String[] acquiredTokens) {
		return tokens.addAll(acquiredTokens);
	}

}
