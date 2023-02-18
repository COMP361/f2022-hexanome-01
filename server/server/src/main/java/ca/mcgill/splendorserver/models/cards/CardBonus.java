package ca.mcgill.splendorserver.models.cards;

import ca.mcgill.splendorserver.models.Token;

public class CardBonus {
	Token type;
	int amount;
	
	public CardBonus(Token type, int amount) {
		this.type = type;
		this.amount = amount;
	}

	public CardBonus() {
		// TODO Auto-generated constructor stub
	}
}
