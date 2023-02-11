package ca.mcgill.splendorserver.models;

import java.util.HashMap;

public class Card {
	
	private int id;
	private CardType type;
	private CardLevel level;
	private int satchelCount;
	private HashMap<Token, Integer> cost;
	
	public Card(int id, int blue, int green, int red, int white, int black, String type, String level) {
		this.id = id;
		cost.put(Token.BLUE, blue);
		cost.put(Token.GREEN, green);
		cost.put(Token.RED, red);
		cost.put(Token.WHITE, white);
		cost.put(Token.BLACK, black);
		this.type = CardType.valueOf(type);
		this.level = CardLevel.valueOf(level);
	}
	
	public CardType getType() {
		return type;
	}
	
	public CardLevel getLevel() {
		return level;
	}
	
	void addSatchel() {
		if (this.type == CardType.SATCHEL) return;
		satchelCount++;
	}
	
	int getSatchelCount() {
		if (this.type == CardType.SATCHEL) return -1;
		return satchelCount;
	}
	
	public HashMap<Token, Integer> getCost() {
		return cost;
	}
	
	public boolean equals(Card other) {
		return id == other.id;
	}
	
}
