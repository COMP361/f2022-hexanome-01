package ca.mcgill.splendorserver.models;

import java.util.HashMap;

public class TokenBank {
	
	private HashMap<Token, Integer> quantities;
	
	public TokenBank() {
		quantities = new HashMap<Token, Integer>();
		for (Token t : Token.values())
			quantities.put(t, 0);
	}
	
	public boolean addAll(String[] tokens) {
		for (String s : tokens) {
			if (!addOne(s)) return false;
		}
		return true;
	}
	
	public boolean addOne(String token) {
		Integer current = quantities.get(Token.valueOf(token));
		if (current == null) return false;
		quantities.put(Token.valueOf(token), current + 1);
		return true;
	}
	
	public boolean removeAll(String[] tokens) {
		for (String s : tokens) {
			if (!removeOne(s)) return false;
		}
		return true;
	}
	
	public boolean removeOne(String token) {
		Integer current = quantities.get(Token.valueOf(token));
		if (current == null || current == 0) return false;
		quantities.put(Token.valueOf(token), current - 1);
		return true;
	}
	
	public boolean canPurchase(Card card) {
		HashMap<Token, Integer> cost = card.getCost();
		for (Token t : Token.values()) {
			if (quantities.get(t) < cost.get(t)) return false;
		}
		return true;
	}
	
	public boolean checkAvailable(String[] tokens) {
		for (Token t : Token.values()) {
			String tokenName = t.toString();
			int quantity = quantities.get(t);
			for (String s : tokens) {
				if (tokenName.equals(s)) quantity--;
			}
			if (quantity < 0) return false;
		}
		return true;
	}

}
